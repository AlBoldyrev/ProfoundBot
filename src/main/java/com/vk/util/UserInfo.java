package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.parser.userInfoParser.Response;
import com.vk.parser.userInfoParser.UserInfoParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfo {

    @Autowired
    private VkApiClient apiClient;

    public String getUserDomain(GroupActor groupActor, int userId) throws ClientException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String s = apiClient.users().get(groupActor).userIds(String.valueOf(userId)).fields(Fields.DOMAIN).executeAsString();
        UserInfoParser userInfoParser = gson.fromJson(s, UserInfoParser.class);
        List<Response> response = userInfoParser.getResponses();
        String userDomain = response.get(0).getDomain();

        return userDomain;

    }
}
