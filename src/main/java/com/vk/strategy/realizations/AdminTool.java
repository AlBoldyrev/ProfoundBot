package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.parser.ModelMessageNew;
import com.vk.strategy.realizations.admintool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminTool {

    @Autowired
    private MessageNew messageNew;

    @Autowired
    private VkApiClient client;

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

    @Autowired
    private Preparation preparation;

    private Gson gson = new GsonBuilder().create();

    void handleMessageNewAsAdmin(JsonObject jsonObject) {

        Map<String, AdminToolResponseHandler> strategyHandlers = new HashMap<>();
        strategyHandlers.put("Сделать связь фоток с музоном по альбомам", albumAudioPhotoCorrelator);
        strategyHandlers.put("Индекс основной папки с фото", indexPhotoInFolderOnServer);
        strategyHandlers.put("Скачать фото и сделать индекс для папки с музоном", indexAudioFromAlbum);
        strategyHandlers.put("Скачать фото и сделать индекс для папки с коммерческим музоном", indexAudioCommerceFromAlbum);
        strategyHandlers.put("Ответить на непрочитанные сообщения", answerToUnreadMessages);
        strategyHandlers.put("Сделать приготовления", preparation);

        ModelMessageNew modelMessageNew = gson.fromJson(jsonObject, ModelMessageNew.class);
        String messageText = modelMessageNew.getObject().getBody();

        AdminToolResponseHandler handler = strategyHandlers.get(messageText);
        if (handler != null) {
            handler.handle();
        }
    }
}

