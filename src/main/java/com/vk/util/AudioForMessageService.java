package com.vk.util;

import com.google.gson.JsonObject;
import com.vk.constants.Constants;
import com.vk.entities.PhotoAudio;
import com.vk.entities.State;
import com.vk.repository.AudioRepository;
import com.vk.repository.PhotoAudioRepository;
import com.vk.repository.StateRepository;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.vk.constants.Constants.KEYBOARD;

@Component
@Slf4j
public class AudioForMessageService {

    @Autowired
    private Constants constants;

    @Autowired
    private PhotoForMessageService photoForMessageService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private PhotoAudioRepository photoAudioRepository;

    private final Random random = new Random();
    private Logger log = LoggerFactory.getLogger(AudioForMessageService.class);

    private String getRandomAudioFromArray(List<String> audioList) {

        String attachment = StringUtil.EMPTY_STRING;

        try {
            if (!audioList.isEmpty()) {
                attachment = audioList.get(random.nextInt(audioList.size()));
            }
        } catch (IllegalArgumentException iae) {
            log.error("Bound exception! " + iae.getStackTrace());
        }

        return attachment;
    }

    public void sendMessageWithProperAudio(JsonObject jsonObject, int userId) {

        String audioNameNC = getAudioName(jsonObject, constants.getUserPhotoFolderPathAudio(), constants.getIndexPathAudio());
        String audioNameC = getAudioName(jsonObject, constants.getUserPhotoFolderPathAudioCommerce(), constants.getIndexPathAudioCommerce());


        if (!audioNameNC.equals(StringUtil.EMPTY_STRING) && !audioNameC.equals(StringUtil.EMPTY_STRING)) {
            if (Math.random() <= 0.1) {
                messageSender.sendMessageWithAttachmentsAndKeyboard(userId, audioNameC, KEYBOARD);

                com.vk.entities.Audio audio = audioRepository.findByAudioName(audioNameC);
                State state = new State(userId, false, audio);
                stateRepository.save(state);

            } else {
                messageSender.sendMessageWithAttachmentsOnly(userId, audioNameNC);
            }
        }

        if (!audioNameNC.equals(StringUtil.EMPTY_STRING) && audioNameC.equals(StringUtil.EMPTY_STRING)) {
            messageSender.sendMessageWithAttachmentsOnly(userId, audioNameNC);
        }

        if (audioNameNC.equals(StringUtil.EMPTY_STRING) && !audioNameC.equals(StringUtil.EMPTY_STRING)) {
            if (Math.random() <= 0.1) {
                messageSender.sendMessageWithAttachmentsAndKeyboard(userId, audioNameC, KEYBOARD);

                com.vk.entities.Audio audio = audioRepository.findByAudioName(audioNameC);
                State state = new State(userId, false, audio);
                stateRepository.save(state);
            }

        }
    }

    private String getAudioName(JsonObject jsonObject, String userPhotoFolderPathAudio, String indexPathAudio) {

        String photoNameForAudio = photoForMessageService.getBestRelatedPhoto(jsonObject, userPhotoFolderPathAudio, indexPathAudio);
        List<PhotoAudio> photoAudioList = photoAudioRepository.findByPhotoName(photoNameForAudio);

        List<String> audioNames = new ArrayList<>();
        String randomAudioName;

        if (photoAudioList != null || !photoAudioList.isEmpty()) {
            for (PhotoAudio photoAudio : photoAudioList) {
                audioNames.add(photoAudio.getAudioName());
            }
            randomAudioName = getRandomAudioFromArray(audioNames);
        } else {
            randomAudioName = StringUtil.EMPTY_STRING;
        }

        return randomAudioName;
    }
}
