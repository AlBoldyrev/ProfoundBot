package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.lirestaff.Indexer;
import com.vk.util.PhotoDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WallPostNew  implements IResponseHandler  {

    @Autowired
    PhotoDownloader photoDownloader;

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws Exception {
//        photoDownloader.downloadPhotosFromAlbum(apiClient, "256054712" , -104375368);
//        Indexer indexer = new Indexer(Constants.photoFolderPath, Constants.reIndexPath);

    }

}
