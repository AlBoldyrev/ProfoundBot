package com.vk.parser;

import com.google.gson.annotations.SerializedName;

public class Peer {

    private int id;
    private String type;
    @SerializedName("local_id")
    private int localId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
}
