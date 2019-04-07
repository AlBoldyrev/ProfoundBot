package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.application.IResponseHandler;

public class MessageAllow implements IResponseHandler {

    public void handle(JsonObject jsonObject, GroupActor groupActor) throws ClientException, ApiException {
        System.out.println("MessageAllow???????");
    }
}