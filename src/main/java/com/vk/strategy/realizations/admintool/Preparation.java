package com.vk.strategy.realizations.admintool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Preparation implements AdminToolResponseHandler {

    @Autowired
    private AlbumAudioPhotoCorrelator albumAudioPhotoCorrelator;

    @Autowired
    private IndexPhotoInFolderOnServer indexPhotoInFolderOnServer;

    @Autowired
    private IndexAudioFromAlbum indexAudioFromAlbum;

    @Autowired
    private IndexAudioCommerceFromAlbum indexAudioCommerceFromAlbum;

    @Autowired
    private AnswerToUnreadMessages answerToUnreadMessages;

    public void handle() {

        indexAudioFromAlbum.handle();
        indexAudioCommerceFromAlbum.handle();
        albumAudioPhotoCorrelator.handle();

    }
}
