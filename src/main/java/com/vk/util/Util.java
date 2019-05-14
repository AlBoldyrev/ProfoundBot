package com.vk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

@Component
public class Util {

    public boolean isAttachmentExists(JsonObject jsonObject) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        boolean isAttachemetExists;

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> myMap = gson.fromJson(jsonObject, type);
        LinkedTreeMap<String, Object> object;
        object = (LinkedTreeMap<String,Object>)myMap.get("object");

        ArrayList<LinkedTreeMap<String,Object>> attachments = (ArrayList<LinkedTreeMap<String,Object>>) object.get("attachments");
        isAttachemetExists = attachments != null && !attachments.isEmpty();

        return isAttachemetExists;
    }

}
