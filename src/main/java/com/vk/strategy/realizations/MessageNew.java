
package com.vk.strategy.realizations;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.queries.photos.PhotosGetAllQuery;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.PhotoParser;
import com.vk.lirestaff.IndexSearcher;
import com.vk.lirestaff.Indexer;
import com.vk.model.message_new.Attachment;
import com.vk.model.message_new.Info;
import com.vk.model.message_new.ModelMessageNew;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;

import com.google.gson.reflect.TypeToken;


@Component
public class MessageNew implements IResponseHandler {

    @Autowired
    UserActor userActor;

    private final Random random = new Random();

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws Exception {

        List<String> idList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);


        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object;
        object = (LinkedTreeMap<String,Object>)myMap.get("object");

        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");

        for (LinkedTreeMap<String,Object> treemaps : attachments) {
            String messageType = (String) treemaps.get("type");
            LinkedTreeMap<String, Object> messageTypeValue;
            messageTypeValue = (LinkedTreeMap<String, Object>) treemaps.get("photo");

            ArrayList<LinkedTreeMap<String,Object>> sizes;
            sizes = (ArrayList<LinkedTreeMap<String,Object>>) messageTypeValue.get("sizes");
            LinkedTreeMap<String, Object> stringObjectLinkedTreeMap = sizes.get(9);
            String url = (String) stringObjectLinkedTreeMap.get("url");

            int from_id = message.getInfo().getFrom_id();

            idList = listOfIdFromSearch(url, from_id);

        }
        List<String> photoNames = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String photoName = idList.get(i);
            photoName = photoName.substring(0, photoName.length() - 5);
            photoNames.add(photoName);
        }
        List<String> test = new ArrayList<>();
        apiClient.messages().send(groupActor).message("s").userId(662638).randomId(random.nextInt()).attachment(test).execute();

       /* String s = apiClient.photos().getMessagesUploadServer(groupActor).executeAsString();
        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(s).getAsJsonObject();
        JsonElement photo = json.get("photo");
        JsonElement server = json.get("server");
        JsonElement hash = json.get("hash");

        apiClient.photos().saveMessagesPhoto(groupActor);*/


    }

    private ArrayList<String> listOfIdFromSearch(String URL, int userId) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile = null;
        BufferedImage img;
        String fileName = null;

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
                System.out.println("Is file deleted? --> " + isFileDeleted);
            }
            fileName = Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
                System.out.println("Is file created? --> " + isFileCreated);
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                System.out.println("Image is null ! ! !");
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName);
            } else {
                System.out.println("UserFile is now exists ! ! !");
                return new ArrayList<>();
            }
        }
        System.out.println( " STRANGE METHOD IS ENDED......");
        if (null != indexSearcher) {
            System.out.println("IndexSearcher is not null :) ");
            return new ArrayList<>(indexSearcher.getIdList());
        } else {
            System.out.println("IndexSearcher is null :( ");
            return new ArrayList<>();
        }

    }
}

