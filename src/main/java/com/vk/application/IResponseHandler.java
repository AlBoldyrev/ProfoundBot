package com.vk.application;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public interface IResponseHandler {

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws ClientException, ApiException, Exception;

}
