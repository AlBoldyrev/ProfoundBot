package com.vk.application;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    BotRequestHandler botHandler;

    @Autowired
    GroupActor groupActor;

    @Autowired
    VkApiClient vk;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            if (!vk.groups().getLongPollSettings(groupActor).execute().isEnabled()) {
                vk.groups().setLongPollSettings(groupActor).enabled(true).wallPostNew(true).execute();
                vk.groups().setLongPollSettings(groupActor).enabled(true).messageNew(true).execute();
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        try {
            botHandler.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }
}
