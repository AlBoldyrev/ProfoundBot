package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.constants.Constants;
import com.vk.jsonphotoparser.Item;
import com.vk.jsonphotoparser.PhotoParser;
import com.vk.jsonphotoparser.Size;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.vk.constants.Constants.MAX_AVAILABLE_PHOTOS_COUNT;

@Component
@Slf4j
public class PhotoDownloader {

    @Autowired
    private UserActor userActor;

    @Autowired
    private GroupActor groupActor;

    private Logger logger = LoggerFactory.getLogger(PhotoDownloader.class);

    public void downloadPhotosFromAlbum(VkApiClient apiClient, String albumId, int groupId, String photoFolderPath) {

        PhotoParser photoParserObjectForDetectionTotalPhotoCount = getPhotoParserObject(apiClient, albumId, groupId, 1, 1);

        int numberOfThousands = photoParserObjectForDetectionTotalPhotoCount.getResponse().getCount()/1000;
        if (numberOfThousands == 0) {
            numberOfThousands = 1;
        }
        int counterOfSkippedPhotos = 1;

        for (int i = 0; i < numberOfThousands+1; i++) {

            PhotoParser photoParserObject = getPhotoParserObject(apiClient, albumId, groupId, MAX_AVAILABLE_PHOTOS_COUNT, i * MAX_AVAILABLE_PHOTOS_COUNT);

            List<Item> items = photoParserObject.getResponse().getItems();
            for (Item item : items) {
                StringBuilder sb = new StringBuilder();
                sb.append("photo").append(item.getOwnerId()).append("_").append(item.getId());
                List<Size> sizes = item.getSizes();
                String url = null;
                InputStream in = null;
                for (Size size: sizes) {
                    if (size.getType().equals("x")) {
                        url = size.getUrl();
                    }
                }
                if (url == null) {
                    url = sizes.get(0).getUrl();
                }
                try {
                    in = new URL(url).openStream();
                    Files.copy(in, Paths.get(photoFolderPath + "\\" + sb + ".jpg"));
                } catch (MalformedURLException me) {
                    logger.error("Malformed exception happened, need something to do!" + me.getStackTrace());
                } catch (IOException ioe) {
                    logger.error("IO exception!!! Something wrong when trying to download photo!" + ioe.getStackTrace());
                }
            }
        }
    }

    public PhotoParser getPhotoParserObject(VkApiClient apiClient, String albumId, int groupId, int photoCount, int offset) {

        String responseWithPhotoUrls = StringUtil.EMPTY_STRING;
        try {
            responseWithPhotoUrls = apiClient.photos().get(userActor).albumId(albumId).count(photoCount).offset(offset).ownerId(groupId).executeAsString();
        } catch (ClientException ce) {
            logger.error("Client exception! Can not execute API CLIENT methods" + Arrays.toString(ce.getStackTrace()));
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(responseWithPhotoUrls).getAsJsonObject();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, PhotoParser.class);
    }
}
