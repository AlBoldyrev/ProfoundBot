
package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.queries.photos.PhotosGetAllQuery;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.PhotoParser;
import com.vk.lirestaff.Indexer;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageNew implements IResponseHandler {

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


        Indexer indexer = new Indexer(Constants.photoFolderPath, Constants.reIndexPath);
    }
}

