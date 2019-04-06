package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.application.IResponseHandler;
import com.vk.jsonaudioparser.Attachment;
import com.vk.jsonaudioparser.Audio;
import com.vk.jsonaudioparser.AudioParser;
import com.vk.jsonaudioparser.Item;
import com.vk.util.PhotoDownloader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageTypingState implements IResponseHandler {


    @Autowired
    PhotoDownloader photoDownloader;

    @Autowired
    UserActor userActor;

    private final Random random = new Random();

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws ClientException, ApiException {
        System.out.println("MessageTypingState???????");


        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<String> audioNames = new ArrayList<>();

        String s = apiClient.wall().get(userActor).ownerId(-170362981).executeAsString();
        AudioParser audioParser = gson.fromJson(s, AudioParser.class);

        List<Item> items = audioParser.getResponse().getItems();
        for (Item item: items) {
            List<Attachment> attachments = item.getAttachments();
            for (Attachment attachment: attachments) {
                Audio audio = attachment.getAudio();
                int ownerId = -170362981;
                int id = audio.getId();
                String audioName = "audio" + ownerId + "_" + id;
                audioNames.add(audioName);
            }
        }
        apiClient.messages().send(groupActor).message("Свежий музон!").userId(662638).randomId(random.nextInt()).attachment(audioNames.get(0)).execute();
        System.out.println("audioNames: " + audioNames);
    }
}
