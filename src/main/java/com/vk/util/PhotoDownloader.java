package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.Item;
import com.vk.jsonphotoparser.PhotoParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class PhotoDownloader {

    @Autowired
    UserActor userActor;

    private final static int MAX_AVAILABLE_PHOTOS_COUNT = 1000;

    public void downloadPhotosFromAlbum(VkApiClient apiClient, String albumId, int groupId) throws ClientException, IOException {

        PhotoParser photoParserObjectForDetectionTotalPhotoCount = getPhotoParserObject(apiClient, albumId, groupId, 1, 1);

        int numberOfThousands = photoParserObjectForDetectionTotalPhotoCount.getResponse().getCount()/1000;
        int counterOfSkippedPhotos = 1;

        for (int i = 0; i < numberOfThousands + 1; i++) {

            PhotoParser photoParserObject = getPhotoParserObject(apiClient, albumId, groupId, MAX_AVAILABLE_PHOTOS_COUNT, i * MAX_AVAILABLE_PHOTOS_COUNT);

            List<Item> items = photoParserObject.getResponse().getItems();
            for (Item item : items) {
                StringBuilder sb = new StringBuilder();
                sb.append("photo").append(item.getOwner_id()).append("_").append(item.getId());
                String photo_604 = item.getPhoto_604();

                try (InputStream in = new URL(photo_604).openStream()) {
                    try {
                        Files.copy(in, Paths.get(Constants.photoFolderPath + "\\" + sb + ".jpg"));
                        System.out.println(sb + " is written");
                    } catch (FileAlreadyExistsException faee) {
                        System.out.println(sb + " is skipped" + " counterOfSkippedPhotos: " + counterOfSkippedPhotos++);
                    }
                }

            }
        }
    }

    private PhotoParser getPhotoParserObject(VkApiClient apiClient, String albumId, int groupId, int photoCount, int offset) throws ClientException {

        String responseWithPhotoUrls = apiClient.photos().get(userActor).albumId(albumId).count(photoCount).offset(offset).ownerId(groupId).executeAsString();

        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(responseWithPhotoUrls).getAsJsonObject();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, PhotoParser.class);
    }
}
