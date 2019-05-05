package com.vk.util;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class MessageSender {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private GroupActor groupActor;

    @Autowired
    private UserInfo userInfo;

    private final Random random = new Random();

    Logger logger = LoggerFactory.getLogger(MessageSender.class);

    public void sendMessageTextOnly(int userId, String message) {

        String userDomain = null;
        userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).execute();
        } catch (ApiException e) {
            logger.error("Something wrong with API: " + e.getMessage());
        } catch (ClientException e) {
            logger.error("Something wrong with CLIENT: " + e.getMessage());
        }

    }

    public void sendMessageWithTextAndAttachements(int userId, String message, String attachments) {

        String userDomain = null;
        userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).attachment(attachments).execute();
        } catch (ApiException e) {
            logger.error("Something wrong with API: " + e.getMessage());
        } catch (ClientException e) {
            logger.error("Something wrong with CLIENT: " + e.getMessage());
        }
    }

    public void sendMessageWithAttachmentsAndKeyboard(int userId, String attachments, String keyboard) {

        String userDomain = null;
        userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                    .domain(userDomain).attachment(attachments).unsafeParam("keyboard", keyboard).execute();
        } catch (ApiException e) {
            logger.error("Something wrong with API: " + e.getMessage());
        } catch (ClientException e) {
            logger.error("Something wrong with CLIENT: " + e.getMessage());
        }
    }

    public void sendMessageWithAttachmentsOnly(int userId, String attachments) {

        String userDomain = null;
        userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                    .domain(userDomain).attachment(attachments).execute();
        } catch (ApiException e) {
            logger.error("Something wrong with API: " + e.getMessage());
        } catch (ClientException e) {
            logger.error("Something wrong with CLIENT: " + e.getMessage());
        }

    }

}
