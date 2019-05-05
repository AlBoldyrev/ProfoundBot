
package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.entities.PhotoAudio;
import com.vk.lirestaff.ListOfPhotosGetter;
import com.vk.parser.*;
import com.vk.repository.PhotoAudioRepository;
import com.vk.util.MessageSender;
import com.vk.util.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.Object;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.vk.constants.Constants.*;

@Component
public class MessageNew implements IResponseHandler {

    @Autowired
    private Constants constants;

    @Autowired
    private UserActor userActor;

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private AdminTool adminTool;

    @Autowired
    private PhotoAudioRepository photoAudioRepository;

    @Autowired
    private ListOfPhotosGetter listOfPhotosGetter;

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private MessageSender messageSender;

    private final Random random = new Random();

    private static final String keyboard = "{ \"one_time\": true, \"buttons\": " +
            "[[{ \"action\": { \"type\": \"text\", \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", " +
            "\"label\": \"Подробнее о музыканте\" }, \"color\": \"default\" }] ] } ";
            Keyboard keyboard2 = new Keyboard();

    public void handle(JsonObject jsonObject, GroupActor groupActor) throws Exception {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);
        int userIdThatSendTheMessage = message.getObject().getUserId();
        String userDomain = userInfo.getUserDomain(groupActor, userIdThatSendTheMessage);


        if (userIdThatSendTheMessage == ALEXANDER_BOLDYREV_VKID) {
           adminTool.handleMessageNewAsAdmin(jsonObject);
        }

        if (isAttachmentExists(jsonObject)) {
            actionIfAttachmentExist(jsonObject, groupActor, userIdThatSendTheMessage);
        } else {
            apiClient.messages().send(groupActor).peerId(userIdThatSendTheMessage).userIds(userIdThatSendTheMessage).randomId(random.nextInt())
                    .domain(userDomain).message("Прости, меня не научили читать текст. Пришли картинку, пожалуйста!").execute();
        }
    }


    private void actionIfAttachmentExist(JsonObject jsonObject, GroupActor groupActor, int userId) throws IOException, ClientException, ApiException {

        //-----------------------------------------------
        //Part for 10 photos

        List<String> photoNames = getPhotoNames(jsonObject, constants.getUserPhotoFolderPath(), constants.getIndexPath(), NUMBER_OF_PHOTOS_IN_THE_MESSAGE);

        String photoNamesString = photoNames.toString();
        String photoNamesWithoutSquareBracketsAndWhitespaces = photoNamesString.substring(1, photoNamesString.length()-1)
                .replaceAll("\\s","");

        //------------------------------------------------
        //Part for audioToSet

        List<String> photoNameForChoosingAudioForAttachment = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudio(),
                constants.getIndexPathAudio(), 1);
        List<String> photoNameForChoosingCommerceAudioForAttachment = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudioCommerce(),
                constants.getIndexPathAudioCommerce(), 1);

        List<String> audioNamesFromAlbumNonCommerce = new ArrayList<>();
        List<String> audioNamesFromAlbumCommerce = new ArrayList<>();

        //------------------------------------------------
        //Part for checking if audio is found

        if (!photoNameForChoosingAudioForAttachment.isEmpty()) {
            List<PhotoAudio> photoAudios = photoAudioRepository.findByPhotoName(photoNameForChoosingAudioForAttachment.get(0));
            for (PhotoAudio photoAudio : photoAudios) {
                audioNamesFromAlbumNonCommerce.add(photoAudio.getAudioName());
            }
        }

        if (!photoNameForChoosingCommerceAudioForAttachment.isEmpty()) {
            List<PhotoAudio> photoAudiosCommerce = photoAudioRepository.findByPhotoName(photoNameForChoosingCommerceAudioForAttachment.get(0));
            for (PhotoAudio photoAudio : photoAudiosCommerce) {
                audioNamesFromAlbumCommerce.add(photoAudio.getAudioName());
            }
        }

        String attachmentsNonCommerce = audioNamesFromAlbumNonCommerce.get(random.nextInt(audioNamesFromAlbumNonCommerce.size()));
        String attachmentsCommerce = audioNamesFromAlbumCommerce.get(random.nextInt(audioNamesFromAlbumCommerce.size()));


        //---------------------------------------------------
        //Part for sending correct message

        messageSender.sendMessageWithAttachmentsOnly(userId, photoNamesWithoutSquareBracketsAndWhitespaces);

        if (!audioNamesFromAlbumNonCommerce.isEmpty()) {
            if (!audioNamesFromAlbumCommerce.isEmpty()) {
                if (random.nextBoolean()) {
                    messageSender.sendMessageWithAttachmentsOnly(userId, attachmentsNonCommerce);
                } else {
                    messageSender.sendMessageWithAttachmentsAndKeyboard(userId, attachmentsCommerce, keyboard);
                }
            } else {
                messageSender.sendMessageWithAttachmentsOnly(userId, attachmentsNonCommerce);
            }
        }
    }

    private List<String> getAudioNames() throws ClientException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        List<String> audioNames = new ArrayList<>();

        String s = apiClient.wall().get(userActor).ownerId(Constants.PUBLIC_ID_WITH_AUDIO_ON_THE_WALL).executeAsString();
        Parser parser = gson.fromJson(s, Parser.class);

        List<Item> items = parser.getResponse().getItems();
        for (Item item: items) {
            List<Attachment> audioAttachments = item.getAttachments();
            for (Attachment attachment: audioAttachments) {

                Audio audio = attachment.getAudio();
                if (audio == null) {
                    continue;
                }
                int ownerId = audio.getOwner_id();
                int id = audio.getId();
                String audioName = "audio" + ownerId + "_" + id;
                audioNames.add(audioName);
            }
        }
        return audioNames;
    }

    private List<String> getPhotoNames(JsonObject jsonObject, String userPhotoFolderPath, String reIndex, int numberOfPhotos) throws IOException {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);

        List<String> idList = new ArrayList<>();

        int senderUserId;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String,Object>)myMap.get("object");
        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");

        for (LinkedTreeMap<String,Object> treeMaps : attachments) {

            LinkedTreeMap<String, Object> messageTypeValue;
            messageTypeValue = (LinkedTreeMap<String, Object>) treeMaps.get("photo");
            String url = messageTypeValue.get("photo_604").toString();
            senderUserId = message.getObject().getUserId();

            idList = listOfPhotosGetter.listOfIdFromSearch(url, senderUserId, userPhotoFolderPath, reIndex);

        }

        List<String> photoNames = new ArrayList<>();
        if (isAttachmentExists(jsonObject)) {
            if (idList.size() > 1) {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = idList.get(i);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
            } else {
                for (int i = 0; i < numberOfPhotos + 1; i++) {
                    String photoName = idList.get(0);
                    photoName = photoName.substring(0, photoName.length() - 4);
                    photoNames.add(photoName);
                }
            }
        }
        return photoNames;

    }

    private boolean isAttachmentExists (JsonObject jsonObject) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        boolean isAttachemetExists;

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object;
        object = (LinkedTreeMap<String,Object>)myMap.get("object");

        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");
        if (attachments == null || attachments.isEmpty()) {
            isAttachemetExists = false;
        } else {
            isAttachemetExists = true;
        }

        return isAttachemetExists;
    }

     ModelMessageNew parseJsonIntoModelMessageNew(JsonObject jsonObject) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonObject, ModelMessageNew.class);
    }

}

