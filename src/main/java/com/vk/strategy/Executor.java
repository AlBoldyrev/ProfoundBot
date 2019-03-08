package com.vk.strategy;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public interface Executor {

    void execute(JsonObject jsonObject, VkApiClient apiClient, GroupActor actor) throws ClientException, ApiException;

}
