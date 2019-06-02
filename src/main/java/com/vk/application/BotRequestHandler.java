package com.vk.application;

import com.google.gson.JsonObject;
import com.vk.api.sdk.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.exceptions.LongPollServerKeyExpiredException;
import com.vk.api.sdk.objects.responses.GetLongPollServerResponse;
import com.vk.strategy.realizations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
class BotRequestHandler {

    @Autowired
    private MessageNew messageNew;

    @Autowired
    private MessageReply messageReply;

    @Autowired
    private WallPostNew wallPostNew;

    @Autowired
    private MessageAllow messageAllow;

    @Autowired
    private MessageTypingState messageTypingState;

    private static final Logger log = LoggerFactory.getLogger(BotRequestHandler.class);
    private static final int DEFAULT_WAIT = 10;

    private final VkApiClient apiClient;

    private final GroupActor groupActor;
    private final Random random = new Random();
    private UserActor userActor;
    private final Integer waitTime;


    @Autowired
    BotRequestHandler(VkApiClient apiClient, GroupActor groupActor) {
        this.apiClient = apiClient;
        this.groupActor = groupActor;
        this.waitTime = DEFAULT_WAIT;
    }

    void run() {

        Map<String, IResponseHandler> strategyHandlers = new HashMap<>();
        strategyHandlers.put("message_new", messageNew);
        strategyHandlers.put("message_reply", messageReply);
        strategyHandlers.put("wall_post_new", wallPostNew);
        strategyHandlers.put("message_allow", messageAllow);
        strategyHandlers.put("message_typing_state", messageTypingState);

        GetLongPollServerResponse longPollServer = null;
        try {
            longPollServer = apiClient.groupsLongPoll().getLongPollServer(groupActor).execute();
        } catch (ApiException e) {
            log.error("API Exception !!! " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("CLIENT Exception !!! " + e.getStackTrace());
        }
        int lastTimeStamp = longPollServer.getTs();


        while (true) try {

            GetLongPollEventsResponse eventsResponse = apiClient.longPoll().getEvents(longPollServer.getServer(),
                    longPollServer.getKey(), lastTimeStamp).waitTime(waitTime).execute();

            for (JsonObject jsonObject : eventsResponse.getUpdates()) {
                String type = jsonObject.get("type").getAsString();
                log.info("jsonType: " + type + "  " + jsonObject);
                IResponseHandler responseHandler = strategyHandlers.get(type);
                try {
                    responseHandler.handle(jsonObject, groupActor);
                } catch (NullPointerException npe) {
                    log.error("This request can not be handled right now." + npe.getStackTrace());
                }

            }
            lastTimeStamp = eventsResponse.getTs();
        } catch (LongPollServerKeyExpiredException | RuntimeException e) {
            try {
                longPollServer = apiClient.groupsLongPoll().getLongPollServer(groupActor).execute();
            } catch (ApiException ex) {
                log.error("API client when trying to connect to LONG POLL server! " + ex.getStackTrace());
            } catch (ClientException ex) {
                log.error("CLIENT client when trying to connect to LONG POLL server! " + ex.getStackTrace());
            }
            log.info(longPollServer.toString());
        } catch (ApiException e) {
            log.error("API client when trying to get LONG POLL server response! " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("CLIENT client when trying to get LONG POLL server response! " + e.getStackTrace());
        }
    }


}
