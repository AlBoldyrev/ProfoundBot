package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.vk.constants.Constants;
import com.vk.lirestaff.ListOfPhotosGetter;
import com.vk.parser.ModelMessageNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.vk.constants.Constants.NUMBER_OF_PHOTOS_IN_THE_MESSAGE;

@Component
public class PhotoForMessageService {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private Constants constants;

    @Autowired
    private Util util;

    @Autowired
    private ListOfPhotosGetter listOfPhotosGetter;


    private Gson gson = new GsonBuilder().create();

    public void sendMessageWithProperPhotos(JsonObject jsonObject, int userId) {

        List<String> photoNames = getPhotoNames(jsonObject, constants.getUserPhotoFolderPath(), constants.getIndexPath(), NUMBER_OF_PHOTOS_IN_THE_MESSAGE);

        String photoNamesString = photoNames.toString();
        String photoNamesWithoutSquareBracketsAndWhitespaces = photoNamesString.substring(1, photoNamesString.length()-1)
                .replaceAll("\\s","");

        messageSender.sendMessageWithAttachmentsOnly(userId, photoNamesWithoutSquareBracketsAndWhitespaces);

    }

    List<String> getPhotoNames(JsonObject jsonObject, String userPhotoFolderPath, String reIndex, int numberOfPhotos) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        List<String> idList;
        int senderUserId;

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String,Object>)myMap.get("object");
        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");

        List<String> photoNames = null;

        for (LinkedTreeMap<String,Object> treeMaps : attachments) {

            LinkedTreeMap<String, Object> messageTypeValue;
            messageTypeValue = (LinkedTreeMap<String, Object>) treeMaps.get("photo");
            String url = messageTypeValue.get("photo_604").toString();
            senderUserId = message.getObject().getUserId();

            idList = listOfPhotosGetter.listOfIdFromSearch(url, senderUserId, userPhotoFolderPath, reIndex);

            photoNames = parseLirePhotoNamesIntoProperArray(jsonObject, idList, numberOfPhotos);
        }


        return photoNames;
    }

    private List<String> parseLirePhotoNamesIntoProperArray(JsonObject jsonObject, List<String> photoNamesAfterLire, int numberOfPhotos) {

        List<String> photoNames = new ArrayList<>();
        if (util.isAttachmentExists(jsonObject)) {
            if (photoNamesAfterLire.size() > 1) {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = photoNamesAfterLire.get(i);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
            } else {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = photoNamesAfterLire.get(0);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
            }
        }
        return photoNames;

    }

    public String getBestRelatedPhoto(JsonObject jsonObject, String userPhotoFolderPath, String reIndex) {

        List<String> photoNames = getPhotoNames(jsonObject, userPhotoFolderPath, reIndex, 1);
        return photoNames.get(0);
    }
}
