package com.vk.util;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MessageSender {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private GroupActor groupActor;

    @Autowired
    private UserInfo userInfo;

    private final Random random = new Random();

    public void sendMessageTextOnly(int userId, String message) throws ClientException, ApiException {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).execute();

    }

    public void sendMessageWithTextAndAttachements(int userId, String message, String attachments) throws ClientException, ApiException {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).attachment(attachments).execute();
    }

    public void sendMessageWithAttachmentsAndKeyboard(int userId, String attachments, String keyboard) throws ClientException, ApiException  {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                .domain(userDomain).attachment(attachments).unsafeParam("keyboard", keyboard).execute();
    }

    public void sendMessageWithAttachmentsOnly(int userId, String attachments) throws ClientException, ApiException {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                .domain(userDomain).attachment(attachments).execute();

    }

}
