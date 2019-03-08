package com.vk.model.message_new;

import com.google.gson.annotations.SerializedName;

public class ModelMessageNew {

    String type;

    @SerializedName("object")
    Info info;

    int group_id;

    public Info getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
