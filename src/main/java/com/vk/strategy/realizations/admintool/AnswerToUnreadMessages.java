package com.vk.strategy.realizations.admintool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AnswerToUnreadMessages implements AdminToolResponseHandler  {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private GroupActor groupActor;

    Logger logger = LoggerFactory.getLogger(AnswerToUnreadMessages.class);

    private final Random random = new Random();
    private final String MESSAGE_TO_USER = "привет) у меня был небольшой технический сбой, но админы меня уже поправили\n"+
            "вроде как, я снова работаю) проверь &#128521;";

    public void handle() {

        logger.info("Job to solve unanswered dialog is started!");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

       /* String s = apiClient.messages().getConversations(groupActor).unanswered1(true).executeAsString();
        Parser parser = gson.fromJson(s, Parser.class);
        List<Item> items = parser.getResponse().getItems();
        int countOfMessages = 0;
        for (Item item : items) {
            int userId = item.getMessage().getUser_id();
            apiClient.messages().send(groupActor).message(MESSAGE_TO_USER).userId(userId).randomId(random.nextInt()).execute();
            countOfMessages++;
            if (countOfMessages == 19) {
                Thread.sleep(3000);
                countOfMessages = 0;
            }
        }*/
    }
}
