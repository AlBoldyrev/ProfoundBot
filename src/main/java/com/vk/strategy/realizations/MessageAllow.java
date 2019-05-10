package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.application.IResponseHandler;
import org.springframework.stereotype.Component;

@Component
public class MessageAllow implements IResponseHandler {

    public void handle(JsonObject jsonObject, GroupActor groupActor)  {
        System.out.println("MessageAllow???????");
    }
}