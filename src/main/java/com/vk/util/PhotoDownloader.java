package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.Item;
import com.vk.jsonphotoparser.PhotoParser;
import com.vk.jsonphotoparser.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.vk.constants.Constants.MAX_AVAILABLE_PHOTOS_COUNT;

@Component
public class PhotoDownloader {

    @Autowired
    private UserActor userActor;

    @Autowired
    private GroupActor groupActor;

    public void downloadPhotosFromAlbum(VkApiClient apiClient, String albumId, int groupId, String photoFolderPath) throws ClientException, IOException {

        PhotoParser photoParserObjectForDetectionTotalPhotoCount = getPhotoParserObject(apiClient, albumId, groupId, 1, 1);

        int numberOfThousands = photoParserObjectForDetectionTotalPhotoCount.getResponse().getCount()/1000;
        if (numberOfThousands == 0) {
            numberOfThousands = 1;
        }
        int counterOfSkippedPhotos = 1;

        for (int i = 0; i < numberOfThousands+1; i++) {

            PhotoParser photoParserObject = getPhotoParserObject(apiClient, albumId, groupId, MAX_AVAILABLE_PHOTOS_COUNT, i * MAX_AVAILABLE_PHOTOS_COUNT);

            List<Item> items = photoParserObject.getResponse().getItems();
            for (Item item : items) {
                StringBuilder sb = new StringBuilder();
                sb.append("photo").append(item.getOwnerId()).append("_").append(item.getId());
                List<Size> sizes = item.getSizes();
                String url = null;
                for (Size size: sizes) {
                    if (size.getType().equals("x")) {
                        url = size.getUrl();
                    }
                }
                if (url == null) {
                    url = sizes.get(0).getUrl();
                }

                try (InputStream in = new URL(url).openStream()) {
                    try {
                        Files.copy(in, Paths.get(photoFolderPath + "\\" + sb + ".jpg"));
                        System.out.println(sb + " is written");
                    } catch (FileAlreadyExistsException faee) {
                        System.out.println(sb + " is skipped" + " counterOfSkippedPhotos: " + counterOfSkippedPhotos++);
                    }
                }

            }
        }
    }

    public PhotoParser getPhotoParserObject(VkApiClient apiClient, String albumId, int groupId, int photoCount, int offset) throws ClientException {

        String responseWithPhotoUrls2 = apiClient.photos().get(userActor).albumId("218340014").count(photoCount).offset(offset).ownerId(662638).executeAsString();
        String responseWithPhotoUrls = apiClient.photos().get(userActor).albumId(albumId).count(photoCount).offset(offset).ownerId(groupId).executeAsString();

        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(responseWithPhotoUrls).getAsJsonObject();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, PhotoParser.class);
    }
}
