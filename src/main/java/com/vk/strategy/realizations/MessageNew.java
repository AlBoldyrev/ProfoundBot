
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
import com.vk.entities.MusicianInfo;
import com.vk.entities.PhotoAudio;
import com.vk.entities.State;
import com.vk.lirestaff.ListOfPhotosGetter;
import com.vk.parser.*;
import com.vk.repository.AudioRepository;
import com.vk.repository.MusicianRepository;
import com.vk.repository.PhotoAudioRepository;
import com.vk.repository.StateRepository;
import com.vk.util.MessageSender;
import com.vk.util.UserInfo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
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

    @Autowired
    MusicianRepository musicianRepository;

    @Autowired
    AudioRepository audioRepository;

    @Autowired
    StateRepository stateRepository;

    private final Random random = new Random();

    public static Logger log = LoggerFactory.getLogger(MessageNew.class);

    private static final String keyboard = "{ \"one_time\": true, \"buttons\": " +
            "[[{ \"action\": { \"type\": \"text\", \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", " +
            "\"label\": \"Подробнее о музыканте\" }, \"color\": \"default\" }] ] } ";
            Keyboard keyboard2 = new Keyboard();

    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);
        int userIdThatSendTheMessage = message.getObject().getUserId();

        if (userIdThatSendTheMessage == ALEXANDER_BOLDYREV_VKID) {
           adminTool.handleMessageNewAsAdmin(jsonObject);
        }

        if (isAttachmentExists(jsonObject)) {
            actionIfAttachmentExist(jsonObject, groupActor, userIdThatSendTheMessage);
        } else {
            if (message.getObject().getBody().equals("Подробнее о музыканте")) {
                try {
                    List<State> stateOfACurrentUser = stateRepository.findByUserId(userIdThatSendTheMessage, false);
                    State stateLast = stateOfACurrentUser.get(stateOfACurrentUser.size() - 1);
                    String audioArtist = stateLast.getAudio().getAudioArtist();
                    MusicianInfo musician = musicianRepository.findByMusicianName(audioArtist);
                    String musicianInfo = musician.getMusicianInfo();
                    Integer numberOfClicks = musician.getNumberOfClicks();
                    musician.setNumberOfClicks(++numberOfClicks);
                    messageSender.sendMessageTextOnly(userIdThatSendTheMessage, musicianInfo);
                    stateLast.setInfoSent(true);
                    musicianRepository.save(musician);
                    stateRepository.save(stateLast);
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    messageSender.sendMessageTextOnly(userIdThatSendTheMessage, "А информации-то вроде как и нет");
                }



            } else {
                messageSender.sendMessageTextOnly(userIdThatSendTheMessage, "Прости, меня не научили читать текст. Пришли картинку, пожалуйста!");
            }
        }
        }


    private void actionIfAttachmentExist(JsonObject jsonObject, GroupActor groupActor, int userId) {

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

        String attachmentsNonCommerce = StringUtil.EMPTY_STRING;
        String attachmentsCommerce = StringUtil.EMPTY_STRING;
        try {
            if (!audioNamesFromAlbumNonCommerce.isEmpty()) {
                attachmentsNonCommerce = audioNamesFromAlbumNonCommerce.get(random.nextInt(audioNamesFromAlbumNonCommerce.size()));
            }
            if (!audioNamesFromAlbumCommerce.isEmpty()) {
                attachmentsCommerce = audioNamesFromAlbumCommerce.get(random.nextInt(audioNamesFromAlbumCommerce.size()));
            }
        } catch (IllegalArgumentException iae) {
            log.error("Bound exception! " + iae.getStackTrace());
        }

        //---------------------------------------------------
        //Part for sending correct message


        messageSender.sendMessageTextOnly(userId, "Просто подборка, без музона:");
        messageSender.sendMessageWithAttachmentsOnly(userId, photoNamesWithoutSquareBracketsAndWhitespaces);
        if (!audioNamesFromAlbumNonCommerce.isEmpty()) {
            if (!audioNamesFromAlbumCommerce.isEmpty()) {
                if (random.nextBoolean()) {
                    messageSender.sendMessageTextOnly(userId, "Подборка с некоммерческим музоном (есть коммерческий):");
                    messageSender.sendMessageWithAttachmentsOnly(userId, attachmentsNonCommerce);
                } else {
                    messageSender.sendMessageTextOnly(userId, "Подборка с коммерческим музоном (есть коммерческий) :");
                    messageSender.sendMessageWithAttachmentsAndKeyboard(userId, attachmentsCommerce, keyboard);

                    com.vk.entities.Audio audio = audioRepository.findByAudioName(attachmentsCommerce);
                    State state = new State(userId, false, audio);
                    stateRepository.save(state);
                }

            } else {
                messageSender.sendMessageTextOnly(userId, "Подборка с некоммерческим музоном (нет коммерческого) :");
                messageSender.sendMessageWithAttachmentsOnly(userId, attachmentsNonCommerce);
            }
        }

    }

    private List<String> getPhotoNames(JsonObject jsonObject, String userPhotoFolderPath, String reIndex, int numberOfPhotos) {

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

