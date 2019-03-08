/*
package com.vk.strategy;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.strategy.realizations.MessageNew;
import com.vk.strategy.realizations.MessageReply;
import com.vk.strategy.realizations.WallPostNew;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.EnumSet;

public enum ActionType {

    wall_post_new(new WallPostNew()),
    message_new(new MessageNew()),
    message_reply(new MessageReply());


    public class Action {
        @Autowired
        private MessageNew messageNew;

        @PostConstruct
        public void postConstruct() {
            for (ActionType actionType : EnumSet.allOf(ActionType.class))
                System.out.println("actionType" + actionType);
        }
    }

    Executor executor;

    ActionType(Executor e){
        this.executor = e;
    }

    public void execute(JsonObject jsonObject, VkApiClient apiClient, GroupActor actor) throws ClientException, ApiException {
        executor.execute(jsonObject, apiClient, actor);
    }

}
*/
