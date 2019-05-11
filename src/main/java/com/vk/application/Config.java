package com.vk.application;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.constants.Constants;
import com.vk.lirestaff.ListOfPhotosGetter;
import com.vk.strategy.realizations.*;
import com.vk.strategy.realizations.admintool.*;
import com.vk.util.MessageSender;
import com.vk.util.PhotoDownloader;
import com.vk.util.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@Slf4j
public class Config {

    private static final String PROPERTIES_FILE = "application.properties";
    private Logger log = LoggerFactory.getLogger(Config.class);

    @Bean
    public HttpTransportClient httpClient() {
        return HttpTransportClient.getInstance();
    }

    @Bean
    public VkApiClient vkApiClient(HttpTransportClient httpClient) {
        return new VkApiClient(httpClient);
    }

    @Bean
    public GroupActor groupActor(Properties properties) {
        return createGroupActor(properties);
    }

    @Bean
    public UserActor userActor(Properties properties) {
        return createUserActor(properties);
    }

    @Bean
    public Properties properties() {
        Properties properties = null;
        try {
            properties = readProperties();
        } catch (IOException ioe) {
            log.error("Can not read properties... :( " + ioe.getStackTrace());
        }
        return properties;
    }

    @Bean
    MessageNew messageNew() {
        return new MessageNew();
    }

    @Bean
    MessageReply messageReply() {
        return new MessageReply();
    }

    @Bean
    WallPostNew wallPostNew() {
        return new WallPostNew();
    }

    @Bean
    MessageAllow messageAllow() {
        return new MessageAllow();
    }

    @Bean
    MessageTypingState messageTypingState() {
        return new MessageTypingState();
    }

    @Bean
    PhotoDownloader photoDownloader() { return new PhotoDownloader();}

    @Bean
    AdminTool adminTool() {return new AdminTool();}

    @Bean
    AlbumAudioPhotoCorrelator albumAudioPhotoCorrelator() {return new AlbumAudioPhotoCorrelator();}

    @Bean
    IndexAudioCommerceFromAlbum indexAudioCommerceFromAlbum() {return new IndexAudioCommerceFromAlbum();}

    @Bean
    IndexAudioFromAlbum indexAudioFromAlbum() {return new IndexAudioFromAlbum();}

    @Bean
    IndexPhotoInFolderOnServer indexPhotoInFolderOnServer() {return new IndexPhotoInFolderOnServer();}

    @Bean
    AnswerToUnreadMessages answerToUnreadMessages() {return  new AnswerToUnreadMessages();}

    @Bean
    ListOfPhotosGetter listOfPhotosGetter() {return new ListOfPhotosGetter();}

    @Bean
    UserInfo userInfo() {return new UserInfo();}

    @Bean
    MessageSender messageSender() {return new MessageSender();}

    @Bean
    Preparation preparation() {return new Preparation();}

    @Bean
    Constants constants(Properties properties) {
        String indexPath = properties.getProperty("indexPath");
        String reIndexPath = properties.getProperty("reIndexPath");
        String photoFolderPath = properties.getProperty("photoFolderPath");
        String userPhotoFolderPath = properties.getProperty("userPhotoFolderPath");
        String indexPathAudio = properties.getProperty("indexPathAudio");
        String reIndexPathAudio = properties.getProperty("reIndexPathAudio");
        String photoFolderPathAudio = properties.getProperty("photoFolderPathAudio");
        String userPhotoFolderPathAudio = properties.getProperty("userPhotoFolderPathAudio");
        String indexPathAudioCommerce = properties.getProperty("indexPathAudioCommerce");
        String reIndexPathAudioCommerce = properties.getProperty("reIndexPathAudioCommerce");
        String photoFolderPathAudioCommerce = properties.getProperty("photoFolderPathAudioCommerce");
        String userPhotoFolderPathAudioCommerce = properties.getProperty("userPhotoFolderPathAudioCommerce");
        String groupIdWithMinus = "-" + properties.getProperty("groupId");
        String albumMusicId = properties.getProperty("albumMusicId");
        String albumMusicCommerceId = properties.getProperty("albumMusicCommerceId");

        System.out.println("It's a " + properties.getProperty("stand"));

        return new Constants(indexPath, reIndexPath, photoFolderPath, userPhotoFolderPath ,indexPathAudio,
                reIndexPathAudio, photoFolderPathAudio, userPhotoFolderPathAudio, indexPathAudioCommerce, reIndexPathAudioCommerce,
                photoFolderPathAudioCommerce, userPhotoFolderPathAudioCommerce, Integer.parseInt(groupIdWithMinus), albumMusicId, albumMusicCommerceId);
    }



    private static GroupActor createGroupActor(Properties properties) {
        String groupId = properties.getProperty("groupId");
        String accessToken = properties.getProperty("token");
        return new GroupActor(Integer.parseInt(groupId), accessToken);
    }

    private static UserActor createUserActor (Properties properties) {
        String userId = properties.getProperty("userId");
        String accessToken = properties.getProperty("userToken");
        return new UserActor(Integer.parseInt(userId), accessToken);
    }

    private static Properties readProperties() throws IOException {
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
        }
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        } finally {
            inputStream.close();
        }
    }
}
