package com.vk.Util;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PhotoDownloader {

    @Autowired
    UserActor userActor;

    public void downloadPhotosFromAlbum(VkApiClient apiClient, String albumId, int groupId) throws ClientException, IOException {

        PhotoParser photoParserObjectForDetectionTotalPhotoCount = getPhotoParserObject(apiClient, albumId, groupId, 1);
        int numberOfThousands = photoParserObjectForDetectionTotalPhotoCount.getResponse().getCount()/1000;
        int counter = 1;

        for (int i = 1; i < numberOfThousands; i++) {

            PhotoParser photoParserObject = getPhotoParserObject(apiClient, albumId, groupId, 999);

            List<Item> items = photoParserObject.getResponse().getItems();
            for (Item item : items) {

                String photo_604 = item.getPhoto_604();
                System.out.println(photo_604);

                try (InputStream in = new URL(photo_604).openStream()) {
                    Files.copy(in, Paths.get(Constants.photoFolderPath + "\\" + counter++ + ".jpg"));
                }

            }
        }
    }

    private PhotoParser getPhotoParserObject(VkApiClient apiClient, String albumId, int groupId, int photoCount) throws ClientException {

        String responseWithPhotoUrls = apiClient.photos().get(userActor).albumId(albumId).count(photoCount).ownerId(groupId).executeAsString();

        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(responseWithPhotoUrls).getAsJsonObject();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        PhotoParser photoParser = gson.fromJson(json, PhotoParser.class);
        return photoParser;
    }
}
