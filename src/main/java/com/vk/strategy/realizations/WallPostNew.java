package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.photos.PhotosGetAllQuery;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.Item;
import com.vk.jsonphotoparser.PhotoParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Component
public class WallPostNew  implements IResponseHandler  {

    @Autowired
    UserActor userActor;

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws Exception {

        PhotosGetAllQuery photosGetAllQuery = apiClient.photos().getAll(userActor).ownerId(-170362981);
        String s = apiClient.photos().getAll(userActor).ownerId(-170362981).executeAsString();
        JsonParser jsonParser = new JsonParser();
        JsonObject objectFromString = jsonParser.parse(s).getAsJsonObject();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PhotoParser photoParser = gson.fromJson(objectFromString, PhotoParser.class);
        List<Item> items = photoParser.getResponse().getItems();
        int counter = 1;
        for (Item item: items) {

            String photo_604 = item.getPhoto_604();
            System.out.println(photo_604);

            try(InputStream in = new URL(photo_604).openStream()){
                String substring = photo_604.substring(8);
                Files.copy(in, Paths.get(Constants.photoFolderPath + "\\" + counter++ + ".jpg"));
            }

        }
    }

}
