package com.vk.strategy.realizations.admintool;

import com.vk.constants.Constants;
import com.vk.lirestaff.Indexer;
import com.vk.util.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IndexPhotoInFolderOnServer implements  AdminToolResponseHandler {

    @Autowired
    private Constants constants;

    Logger logger = LoggerFactory.getLogger(IndexPhotoInFolderOnServer.class);

    public void handle() throws IOException {
        logger.info("Indexing photo in folder on server is starting!");
        Indexer indexer = new Indexer(constants.getPhotoFolderPath(), constants.getReIndexPath());
    }

}
