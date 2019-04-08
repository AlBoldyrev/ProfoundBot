
package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.jsonaudioparser.Audio;
import com.vk.jsonaudioparser.AudioParser;
import com.vk.jsonaudioparser.Item;
import com.vk.lirestaff.IndexSearcher;
import com.vk.model.message_new.ModelMessageNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.vk.constants.Constants.NUMBER_OF_PHOTOS_IN_THE_MESSAGE;


@Component
public class MessageNew implements IResponseHandler {

    @Autowired
    UserActor userActor;

    @Autowired
    VkApiClient apiClient;
    
    private final Random random = new Random();

    public void handle(JsonObject jsonObject, GroupActor groupActor) throws Exception {

        List<String> audioNames = getAudioNames();
        List<String> photoNames = getPhotoNames(jsonObject);

        if (isAttachmentExists(jsonObject)) {
            apiClient.messages().send(groupActor).message("Свежая подборочка!").userId(662638).randomId(random.nextInt()).attachment(photoNames).execute();
        }
        apiClient.messages().send(groupActor).userId(662638).randomId(random.nextInt()).attachment(audioNames.get(random.nextInt(audioNames.size()))).execute();

    }

    private ArrayList<String> listOfIdFromSearch(String URL, int userId) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile;
        BufferedImage img;
        String fileName;

        File folder = new File(Constants.userPhotoFolderPath + userId);
        if (!folder.exists()) {
            boolean isFolderCreated = folder.mkdir();
            System.out.println("Is folder created? --> " + isFolderCreated);
        }

        try(InputStream in = new URL(URL).openStream()){
            try {
                Files.copy(in, Paths.get(Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png"));
            } catch (FileAlreadyExistsException faee) {
                System.out.println("Photo is already exists");
            }
        }

        File[] files = new File(Constants.userPhotoFolderPath + userId).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                boolean isFileDeleted = file.delete();
            }
            fileName = Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                System.out.println("Image is null! ");
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName);
            } else {
                System.out.println("UserFile is now exists! ");
                return new ArrayList<>();
            }
        }
        if (null != indexSearcher) {
            System.out.println("IndexSearcher worked well :) ");
            return new ArrayList<>(indexSearcher.getIdList());
        } else {
            System.out.println("IndexSearcher is null :( ");
            return new ArrayList<>();
        }
    }

    private List<String> getAudioNames() throws ClientException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        List<String> audioNames = new ArrayList<>();

        String s = apiClient.wall().get(userActor).ownerId(Constants.PUBLIC_ID_WITH_AUDIO_ON_THE_WALL).executeAsString();
        AudioParser audioParser = gson.fromJson(s, AudioParser.class);

        List<Item> items = audioParser.getResponse().getItems();
        for (Item item: items) {
            List<com.vk.jsonaudioparser.Attachment> audioAttachments = item.getAttachments();
            for (com.vk.jsonaudioparser.Attachment attachment: audioAttachments) {
                Audio audio = attachment.getAudio();
                int ownerId = audio.getOwner_id();
                int id = audio.getId();
                String audioName = "audio" + ownerId + "_" + id;
                audioNames.add(audioName);
            }
        }
        return audioNames;
    }

    private List<String> getPhotoNames(JsonObject jsonObject) throws IOException {

        List<String> idList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int senderUserId;

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String,Object>)myMap.get("object");
        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");

        for (LinkedTreeMap<String,Object> treeMaps : attachments) {

            LinkedTreeMap<String, Object> messageTypeValue;
            messageTypeValue = (LinkedTreeMap<String, Object>) treeMaps.get("photo");

            ArrayList<LinkedTreeMap<String,Object>> sizes = (ArrayList<LinkedTreeMap<String,Object>>) messageTypeValue.get("sizes");
            LinkedTreeMap<String, Object> stringObjectLinkedTreeMap = sizes.get(sizes.size() - 1);
            String url = (String) stringObjectLinkedTreeMap.get("url");

            senderUserId = message.getInfo().getFrom_id();

            idList = listOfIdFromSearch(url, senderUserId);

        }

        List<String> photoNames = new ArrayList<>();
        if (isAttachmentExists(jsonObject)) {
            for (int i = 1; i < NUMBER_OF_PHOTOS_IN_THE_MESSAGE + 1; i++) {
                String photoName = idList.get(i);
                photoName = photoName.substring(0, photoName.length() - 4);
                photoNames.add(photoName);
            }
        }
        return photoNames;

    }

    private boolean isAttachmentExists (JsonObject jsonObject) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object;
        object = (LinkedTreeMap<String,Object>)myMap.get("object");

        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");
        return !attachments.isEmpty();
    }

}

