package com.vk.parser.messageNew;

import com.google.gson.annotations.SerializedName;

public class EventParser {

    private String type;

    private Object object;

    @SerializedName("group_id")
    private Integer groupId;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
