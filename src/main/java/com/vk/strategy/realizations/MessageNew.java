
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
import com.vk.lirestaff.IndexSearcher;
import com.vk.lirestaff.Indexer;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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

    private ArrayList<String> listOfIdFromSearch(String URL, String userId) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile = null;
        BufferedImage img;
        String fileName = null;

        File folder = new File(Constants.userPhotoFolderPath + userId);
        if (!folder.exists()) {
            boolean isFolderCreated = folder.mkdir();
            System.out.println("Is folder created? --> " + isFolderCreated);
        }

        File[] files = new File(Constants.userPhotoFolderPath + userId).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                boolean isFileDeleted = file.delete();
                System.out.println("Is file deleted? --> " + isFileDeleted);
            }
            fileName = Constants.userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
                System.out.println("Is file created? --> " + isFileCreated);
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                System.out.println("Image is null ! ! !");
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName);
            } else {
                System.out.println("UserFile is now exists ! ! !");
                return new ArrayList<>();
            }
        }

        if (null != indexSearcher) {
            System.out.println("IndexSearcher is not null :) ");
            return new ArrayList<>(indexSearcher.getIdList());
        } else {
            System.out.println("IndexSearcher is null :( ");
            return new ArrayList<>();
        }

    }
}

