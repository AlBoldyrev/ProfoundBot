
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

    private static final int NUMBER_OF_PHOTOS = 10;
    private final Random random = new Random();

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws Exception {

        List<String> idList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int senderUserId;

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object;
        object = (LinkedTreeMap<String,Object>)myMap.get("object");

        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");

        for (LinkedTreeMap<String,Object> treemaps : attachments) {
            LinkedTreeMap<String, Object> messageTypeValue;
            messageTypeValue = (LinkedTreeMap<String, Object>) treemaps.get("photo");

            ArrayList<LinkedTreeMap<String,Object>> sizes;
            sizes = (ArrayList<LinkedTreeMap<String,Object>>) messageTypeValue.get("sizes");
            LinkedTreeMap<String, Object> stringObjectLinkedTreeMap = sizes.get(sizes.size() - 1);
            String url = (String) stringObjectLinkedTreeMap.get("url");

            senderUserId = message.getInfo().getFrom_id();

            idList = listOfIdFromSearch(url, senderUserId);

        }
        List<String> photoNames = new ArrayList<>();
        if (!attachments.isEmpty()) {
            for (int i = 1; i < NUMBER_OF_PHOTOS; i++) {
                String photoName = idList.get(i);
                photoName = photoName.substring(0, photoName.length() - 5);
                photoNames.add(photoName);
            }
            apiClient.messages().send(groupActor).message("Свежая подборочка!").userId(662638).randomId(random.nextInt()).attachment("audio2000313711_456242671").execute();
        }
    }

    private ArrayList<String> listOfIdFromSearch(String URL, int userId) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile;
        BufferedImage img;
        String fileName;

        try(InputStream in = new URL(URL).openStream()){
            try {
                Files.copy(in, Paths.get(Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png"));
            } catch (FileAlreadyExistsException faee) {
                System.out.println("Photo is already exists");
            }
        }

        fileName = Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png";
        img = ImageIO.read(new URL(URL));
        userFile = new File(fileName);

        if (img != null) {
            ImageIO.write(img, "png", userFile);
        } else {
            return new ArrayList<>();
        }

        if (userFile.exists()) {
            indexSearcher = new IndexSearcher(fileName);
        } else {
            return new ArrayList<>();
        }

        return new ArrayList<>(indexSearcher.getIdList());
    }

}

