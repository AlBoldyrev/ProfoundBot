package com.vk.strategy.realizations.admintool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.parser.Item;
import com.vk.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class AnswerToUnreadMessages  implements  AdminToolResponseHandler  {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private GroupActor groupActor;

    private final Random random = new Random();
    private final String MESSAGE_TO_USER = "привет) у меня был небольшой технический сбой, но админы меня уже поправили\n"+
            "вроде как, я снова работаю) проверь &#128521;";

    public void handle() throws ClientException, ApiException, InterruptedException {


        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String s = apiClient.messages().getDialogs(groupActor).unanswered1(true).executeAsString();
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
        }
    }
}
