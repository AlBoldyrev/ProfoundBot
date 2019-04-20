package com.vk.strategy.realizations.admintool;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.constants.Constants;
import com.vk.lirestaff.Indexer;
import com.vk.util.PhotoDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IndexAudioFromAlbum implements  AdminToolResponseHandler {

    @Autowired
    private PhotoDownloader photoDownloader;

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private Constants constants;

    public void handle() throws IOException, ClientException {

        photoDownloader.downloadPhotosFromAlbum(apiClient, constants.getAlbumMusicId(), constants.getGroupIdWithMinus(),
                constants.getPhotoFolderPathAudio());
        Indexer indexer = new Indexer(constants.getPhotoFolderPathAudio(), constants.getReIndexPathAudio());
    }
}
