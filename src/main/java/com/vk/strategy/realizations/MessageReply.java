package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.application.IResponseHandler;
import org.springframework.stereotype.Component;


@Component
public class MessageReply implements IResponseHandler {

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) {

    }
}