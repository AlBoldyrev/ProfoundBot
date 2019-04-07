package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.application.IResponseHandler;
import com.vk.util.PhotoDownloader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class MessageTypingState implements IResponseHandler {

    public void handle(JsonObject jsonObject, GroupActor groupActor) {

    }
}
