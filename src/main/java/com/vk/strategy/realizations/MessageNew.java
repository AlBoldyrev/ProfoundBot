
package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.application.IResponseHandler;
import com.vk.constants.Constants;
import com.vk.entities.MusicianInfo;
import com.vk.entities.State;
import com.vk.lirestaff.ListOfPhotosGetter;
import com.vk.parser.ModelMessageNew;
import com.vk.repository.AudioRepository;
import com.vk.repository.MusicianRepository;
import com.vk.repository.PhotoAudioRepository;
import com.vk.repository.StateRepository;
import com.vk.util.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.vk.constants.Constants.ALEXANDER_BOLDYREV_VKID;
import static com.vk.constants.Constants.VASILII_KALITEEVSKY_VKID;

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
    private MusicianRepository musicianRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private Util util;

    @Autowired
    private AudioForMessageService audioForMessageService;

    @Autowired
    private PhotoForMessageService photoForMessageService;
    private Gson gson = new GsonBuilder().create();

    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int userIdThatSendTheMessage = message.getObject().getUserId();

        if (util.isAttachmentExists(jsonObject)) {
            actionIfAttachmentExist(jsonObject, userIdThatSendTheMessage);
        } else {
            if (message.getObject().getBody().equals("Подробнее о музыканте")) {
                try {

                    List<State> stateOfACurrentUser = stateRepository.findByUserId(userIdThatSendTheMessage, false);
                    State stateLast = stateOfACurrentUser.get(stateOfACurrentUser.size() - 1);
                    String audioArtist = stateLast.getAudio().getAudioArtist();
                    MusicianInfo musician = musicianRepository.findByMusicianName(audioArtist);
                    String musicianInfo = musician.getMusicianInfo();
                    String replace = musicianInfo.replace("\\n", "\n");
                    Integer numberOfClicks = musician.getNumberOfClicks();
                    musician.setNumberOfClicks(++numberOfClicks);
                    messageSender.sendMessageTextOnly(userIdThatSendTheMessage, replace);
                    stateLast.setInfoSent(true);
                    musicianRepository.save(musician);
                    stateRepository.save(stateLast);

                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    messageSender.sendMessageTextOnly(userIdThatSendTheMessage, "А информации-то вроде как и нет");
                }
            } else {
                if (userIdThatSendTheMessage == ALEXANDER_BOLDYREV_VKID || userIdThatSendTheMessage == VASILII_KALITEEVSKY_VKID) {
                    adminTool.handleMessageNewAsAdmin(jsonObject);
                    return;
                }
                messageSender.sendMessageTextOnly(userIdThatSendTheMessage, "Прости, меня не научили читать текст. Пришли картинку, пожалуйста!");
            }
        }
    }


    private void actionIfAttachmentExist(JsonObject jsonObject, int userId) {

        photoForMessageService.sendMessageWithProperPhotos(jsonObject, userId);
        audioForMessageService.sendMessageWithProperAudio(jsonObject, userId);

    }
}



