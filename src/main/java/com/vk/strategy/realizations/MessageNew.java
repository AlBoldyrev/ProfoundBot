
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
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.queries.Field;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.entities.PhotoAudio;
import com.vk.parser.*;
import com.vk.lirestaff.IndexSearcher;
import com.vk.parser.messageNew.EventParser;
import com.vk.parser.userInfoParser.Response;
import com.vk.parser.userInfoParser.UserInfoParser;
import com.vk.repository.PhotoAudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Object;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private final Random random = new Random();

    public static final String keyB =            "{ \"one_time\": true, \"buttons\": " +
            "                    [[{ \"action\": { \"type\": \"text\", \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", \"label\": \"Подробнее о музыканте\" }, \"color\": \"default\" }] ] } ";
            Keyboard keyboard2 = new Keyboard();

    public void handle(JsonObject jsonObject, GroupActor groupActor) throws Exception {

        ModelMessageNew message = parseJsonIntoModelMessageNew(jsonObject);
        int userIdThatSendTheMessage = message.getObject().getUserId();
        String userDomain = getUserDomain(groupActor, userIdThatSendTheMessage);


        if (userIdThatSendTheMessage == ALEXANDER_BOLDYREV_VKID) {
           adminTool.handleMessageNewAsAdmin(jsonObject);
        }

        if (isAttachmentExists(jsonObject)) {
            actionIfAttachmentExist(jsonObject, groupActor, userIdThatSendTheMessage);
        } else {
            apiClient.messages().send(groupActor).peerId(userIdThatSendTheMessage).userIds(userIdThatSendTheMessage).randomId(random.nextInt())
                    .domain(userDomain).message("Прости, меня не научили читать текст. Пришли картинку, пожалуйста!").unsafeParam("keyboard" , keyB).execute();
        }
    }


    private void actionIfAttachmentExist(JsonObject jsonObject, GroupActor groupActor, int userId) throws IOException, ClientException, ApiException {

        String userDomain = getUserDomain(groupActor, userId);

        List<String> photoNames = getPhotoNames(jsonObject, constants.getUserPhotoFolderPath(), constants.getIndexPath(), NUMBER_OF_PHOTOS_IN_THE_MESSAGE);

        List<String> photoNameForChoosingAudioForAttachment = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudio(),
                constants.getIndexPathAudio(), 1);
        List<String> photoNameForChoosingCommerceAudioForAttachment = getPhotoNames(jsonObject, constants.getUserPhotoFolderPathAudioCommerce(),
                constants.getIndexPathAudioCommerce(), 1);

        List<String> audioNamesFromAlbum = new ArrayList<>();
        List<String> audioNamesFromAlbumCommerce = new ArrayList<>();


        if (!photoNameForChoosingAudioForAttachment.isEmpty()) {
            List<PhotoAudio> photoAudios = photoAudioRepository.findByPhotoName(photoNameForChoosingAudioForAttachment.get(0));
            for (PhotoAudio photoAudio : photoAudios) {
                audioNamesFromAlbum.add(photoAudio.getAudioName());
            }
        }

        if (!photoNameForChoosingCommerceAudioForAttachment.isEmpty()) {
            List<PhotoAudio> photoAudiosCommerce = photoAudioRepository.findByPhotoName(photoNameForChoosingCommerceAudioForAttachment.get(0));
            for (PhotoAudio photoAudio : photoAudiosCommerce) {
                audioNamesFromAlbumCommerce.add(photoAudio.getAudioName());
            }
        }

        String s1 = photoNames.toString();
        String s3 = s1.substring(1, s1.length()-1);
        s3 = s3.replaceAll("\\s","");

        if (isAttachmentExists(jsonObject)) {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt()).domain(userDomain).attachment(s3).execute();
        }

        if (!audioNamesFromAlbum.isEmpty()) {

            if (!audioNamesFromAlbumCommerce.isEmpty()) {

                if (random.nextBoolean()) {
                    apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                            .domain(userDomain).attachment(audioNamesFromAlbum.get(random.nextInt(audioNamesFromAlbum.size()))).execute();
                } else {
                    apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                            .domain(userDomain).attachment(audioNamesFromAlbumCommerce.get(random.nextInt(audioNamesFromAlbumCommerce.size()))).execute();
                }

            } else {
                apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                        .domain(userDomain).attachment(audioNamesFromAlbum.get(random.nextInt(audioNamesFromAlbum.size()))).execute();

            }
        }
    }

    private String getUserDomain(GroupActor groupActor, int userId) throws ClientException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String s = apiClient.users().get(groupActor).userIds(String.valueOf(userId)).fields(Fields.DOMAIN).executeAsString();
        UserInfoParser userInfoParser = gson.fromJson(s, UserInfoParser.class);
        List<Response> response = userInfoParser.getResponses();
        String userDomain = response.get(0).getDomain();

        return userDomain;

    }
    private ArrayList<String> listOfIdFromSearch(String URL, int userId, String userPhotoFolderPath, String reIndex) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile;
        BufferedImage img;
        String fileName;

        File folder = new File(userPhotoFolderPath + userId);
        if (!folder.exists()) {
            boolean isFolderCreated = folder.mkdir();
        }

        try(InputStream in = new URL(URL).openStream()){
            try {
                Files.copy(in, Paths.get(userPhotoFolderPath + userId + "\\" + userId + "&1.png"));
            } catch (FileAlreadyExistsException faee) {
            }
        }

        File[] files = new File(userPhotoFolderPath + userId).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                boolean isFileDeleted = file.delete();
            }
            fileName = userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName, reIndex);
            } else {
                return new ArrayList<>();
            }
        }
        if (null != indexSearcher) {
            System.out.println("IndexSearcher worked well :) ");
            return new ArrayList<>(indexSearcher.getIdList());
        } else {
            System.out.println("IndexSearcher is null :( ");
            return new ArrayList<>();
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

            idList = listOfIdFromSearch(url, senderUserId, userPhotoFolderPath, reIndex);

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

