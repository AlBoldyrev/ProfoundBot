
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
import com.vk.entities.PhotoAudio;
import com.vk.parser.Audio;
import com.vk.parser.Parser;
import com.vk.parser.Item;
import com.vk.lirestaff.IndexSearcher;
import com.vk.model.message_new.ModelMessageNew;
import com.vk.repository.PhotoAudioRepository;
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

import static com.vk.constants.Constants.*;

@Component
public class MessageNew implements IResponseHandler {

    @Autowired
    private Constants constants;

    @Autowired
    private UserActor userActor;

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private AdminTool adminTool;

    @Autowired
    private PhotoAudioRepository photoAudioRepository;

    private final Random random = new Random();

    public void handle(JsonObject jsonObject, GroupActor groupActor) throws Exception {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);
        int userIdThatSendTheMessage = message.getInfo().getFrom_id();

        if (userIdThatSendTheMessage == ALEXANDER_BOLDYREV_VKID) {
           adminTool.handleMessageNewAsAdmin(jsonObject);
        }

        List<String> photoNames = getPhotoNames(jsonObject, constants.getUserPhotoFolderPath(), constants.getIndexPath(), NUMBER_OF_PHOTOS_IN_THE_MESSAGE);
        System.out.println("1");
        List<String> photoNamesAudio = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudio(), constants.getIndexPathAudio(), 1);
        System.out.println("2");
        List<String> photoNamesAudioCommerce = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudioCommerce(), constants.getIndexPathAudioCommerce(), 1);
        System.out.println("3");
        List<PhotoAudio> photoAudios;
        List<PhotoAudio> photoAudiosCommerce;
        List<String> audioNamesFromAlbum = new ArrayList<>();
        List<String> audioNamesFromAlbumCommerce = new ArrayList<>();
        if (!photoNamesAudio.isEmpty()) {
            photoAudios = photoAudioRepository.findByPhotoName(photoNamesAudio.get(0));
            for (PhotoAudio photoAudio : photoAudios) {
                audioNamesFromAlbum.add(photoAudio.getAudioName());
            }
        }

        if (!photoNamesAudioCommerce.isEmpty()) {
            photoAudiosCommerce = photoAudioRepository.findByPhotoName(photoNamesAudioCommerce.get(0));
            for (PhotoAudio photoAudio : photoAudiosCommerce) {
                audioNamesFromAlbumCommerce.add(photoAudio.getAudioName());
            }
        }

        if (isAttachmentExists(jsonObject)) {
            apiClient.messages().send(groupActor).userId(userIdThatSendTheMessage).randomId(random.nextInt()).attachment(photoNames).execute();
        }

        if (!audioNamesFromAlbum.isEmpty()) {

            if (!audioNamesFromAlbumCommerce.isEmpty()) {

                if (random.nextBoolean()) {
                    apiClient.messages().send(groupActor).userId(userIdThatSendTheMessage).randomId(random.nextInt()).attachment(audioNamesFromAlbum.get(random.nextInt(audioNamesFromAlbum.size()))).execute();
                } else {
                    apiClient.messages().send(groupActor).userId(userIdThatSendTheMessage).randomId(random.nextInt()).attachment(audioNamesFromAlbumCommerce.get(random.nextInt(audioNamesFromAlbumCommerce.size()))).execute();
                }

            } else {
                apiClient.messages().send(groupActor).userId(userIdThatSendTheMessage).randomId(random.nextInt()).attachment(audioNamesFromAlbum.get(random.nextInt(audioNamesFromAlbum.size()))).execute();
            }

        }
    }

    private ArrayList<String> listOfIdFromSearch(String URL, int userId, String userPhotoFolderPath, String reIndex) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile;
        BufferedImage img;
        String fileName;

        File folder = new File(userPhotoFolderPath + userId);
        if (!folder.exists()) {
            boolean isFolderCreated = folder.mkdir();
        }

        try(InputStream in = new URL(URL).openStream()){
            try {
                Files.copy(in, Paths.get(userPhotoFolderPath + userId + "\\" + userId + "&1.png"));
            } catch (FileAlreadyExistsException faee) {
            }
        }

        File[] files = new File(userPhotoFolderPath + userId).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                boolean isFileDeleted = file.delete();
            }
            fileName = userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName, reIndex);
            } else {
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
        Parser parser = gson.fromJson(s, Parser.class);

        List<Item> items = parser.getResponse().getItems();
        for (Item item: items) {
            List<com.vk.parser.Attachment> audioAttachments = item.getAttachments();
            for (com.vk.parser.Attachment attachment: audioAttachments) {

                Audio audio = attachment.getAudio();
                if (audio == null) {
                    continue;
                }
                int ownerId = audio.getOwner_id();
                int id = audio.getId();
                String audioName = "audio" + ownerId + "_" + id;
                audioNames.add(audioName);
            }
        }
        return audioNames;
    }

    private List<String> getPhotoNames(JsonObject jsonObject, String userPhotoFolderPath, String reIndex, int numberOfPhotos) throws IOException {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);

        List<String> idList = new ArrayList<>();

        int senderUserId;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

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

            idList = listOfIdFromSearch(url, senderUserId, userPhotoFolderPath, reIndex);

        }

        List<String> photoNames = new ArrayList<>();
        if (isAttachmentExists(jsonObject)) {
            if (idList.size() > 1) {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = idList.get(i);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
            } else {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = idList.get(0);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
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

     ModelMessageNew parseJsonIntoModelMessageNew(JsonObject jsonObject) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonObject, ModelMessageNew.class);
    }

}

