package com.vk.strategy.realizations.admintool;

import com.vk.constants.Constants;
import com.vk.lirestaff.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IndexPhotoInFolderOnServer implements  AdminToolResponseHandler {

    @Autowired
    private Constants constants;

    public void handle() throws IOException {
        Indexer indexer = new Indexer(constants.getPhotoFolderPath(), constants.getReIndexPath());
    }

}
