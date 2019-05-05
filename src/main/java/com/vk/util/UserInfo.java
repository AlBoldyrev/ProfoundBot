package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.parser.userInfoParser.Response;
import com.vk.parser.userInfoParser.UserInfoParser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserInfo {

    @Autowired
    private VkApiClient apiClient;

    Logger logger = LoggerFactory.getLogger(UserInfo.class);

    public String getUserDomain(GroupActor groupActor, int userId) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String domainName = null;
        try {
            domainName = apiClient.users().get(groupActor).userIds(String.valueOf(userId)).fields(Fields.DOMAIN).executeAsString();
        } catch (ClientException e) {
            logger.error("Can not get userDomain :( " + e.getMessage());
        }
        UserInfoParser userInfoParser = gson.fromJson(domainName, UserInfoParser.class);
        List<Response> response = userInfoParser.getResponses();
        String userDomain = response.get(0).getDomain();

        return userDomain;

    }
}
