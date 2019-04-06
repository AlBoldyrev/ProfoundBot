package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.jsonaudioparser.Attachment;
import com.vk.jsonaudioparser.Audio;
import com.vk.jsonaudioparser.AudioParser;
import com.vk.jsonaudioparser.Item;
import com.vk.lirestaff.Indexer;
import com.vk.util.PhotoDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class WallPostNew  implements IResponseHandler  {

    @Autowired
    PhotoDownloader photoDownloader;

    @Autowired
    UserActor userActor;

    private final Random random = new Random();

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws Exception {
//        photoDownloader.downloadPhotosFromAlbum(apiClient, "256054712" , -104375368);
//        Indexer indexer = new Indexer(Constants.photoFolderPath, Constants.reIndexPath);



    }

}
