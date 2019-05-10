package com.vk.strategy.realizations.admintool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.constants.Constants;
import com.vk.entities.Audio;
import com.vk.entities.PhotoAudio;
import com.vk.jsonphotoparser.Item;
import com.vk.jsonphotoparser.PhotoParser;
import com.vk.parser.Attachment;
import com.vk.parser.Parser;
import com.vk.repository.AudioRepository;
import com.vk.repository.PhotoAudioRepository;
import com.vk.util.MessageSender;
import com.vk.util.PhotoDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.vk.constants.Constants.MAX_AVAILABLE_PHOTOS_COUNT;

@Component
public class AlbumAudioPhotoCorrelator implements  AdminToolResponseHandler {

    @Autowired
    private PhotoDownloader photoDownloader;

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private Constants constants;

    @Autowired
    private UserActor userActor;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private PhotoAudioRepository photoAudioRepository;



    Logger logger = LoggerFactory.getLogger(AlbumAudioPhotoCorrelator.class);

    public void handle() {

        logger.info("Album Audio And Photo Correlator is running ! ");

        correlateAudioAndPhotoInAlbum(constants.getAlbumMusicId());
        correlateAudioAndPhotoInAlbum(constants.getAlbumMusicCommerceId());
    }

    public void correlateAudioAndPhotoInAlbum (String albumId) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        PhotoParser photoParserObjectForDetectionTotalPhotoCount = photoDownloader.getPhotoParserObject(apiClient,
                albumId, constants.getGroupIdWithMinus(), 1, 1);
        int numberOfThousands = photoParserObjectForDetectionTotalPhotoCount.getResponse().getCount() / 1000;

        for (int i = 0; i < numberOfThousands + 1; i++) {

            PhotoParser photoParserObject = photoDownloader.getPhotoParserObject(apiClient, albumId,
                    constants.getGroupIdWithMinus(), MAX_AVAILABLE_PHOTOS_COUNT, i * MAX_AVAILABLE_PHOTOS_COUNT);

            List<Item> items = photoParserObject.getResponse().getItems();
            for (Item item : items) {

                int photoId = item.getId();
                int photoOwnerId = item.getOwnerId();

                String photoName = "photo" + photoOwnerId + "_" + photoId;
                String s = null;
                try {
                    s = apiClient.photos().getComments(userActor, photoId).ownerId(constants.getGroupIdWithMinus()).count(5).executeAsString();
                } catch (ClientException e) {
                    logger.error("CLIENT exception happened when trying to get comments to photos in public album " + e.getStackTrace());
                }

                Parser parser = gson.fromJson(s, Parser.class);

                List<com.vk.parser.Item> itemz = parser.getResponse().getItems();

                for (com.vk.parser.Item commentItem : itemz) {
                    List<Attachment> attachments = commentItem.getAttachments();

                    List<String> audioNames = new ArrayList<>();
                    List<Audio> audios = new ArrayList<>();

                    Map<String, Map<Integer, Integer>> audioMap = new HashMap<>();

                    for (Attachment attachment : attachments) {
                        String attachmentType = attachment.getType();
                        if (attachmentType.equals("audio")) {
                            int ownerId = attachment.getAudio().getOwner_id();
                            int audioId = attachment.getAudio().getId();
                            String audioArtist = attachment.getAudio().getArtist();
                            String audioTitle = attachment.getAudio().getTitle();



                            String audioName = "audio" + ownerId + "_" + audioId;

                            Audio audio = new Audio(audioArtist, audioTitle, audioId, ownerId);
                            audio.setAudioName(audioName);
                            audios.add(audio);
                            audioRepository.save(audio);

                            Map<Integer, Integer> ownerIdsAudioIds = Collections.singletonMap(ownerId, audioId);
                            audioMap.put(audioName, ownerIdsAudioIds);

                            audioNames.add(audioName);
                        }
                    }

                    for (String audioName : audioNames) {
                        PhotoAudio photoAudio = new PhotoAudio();
                        photoAudio.setPhotoName(photoName);
                        photoAudio.setAudioName(audioName);

                        Audio audio = audioRepository.findByAudioName(audioName);
                        photoAudio.setAudio(audio);
                        photoAudioRepository.save(photoAudio);
                    }

                }
            }
        }
    }
}
