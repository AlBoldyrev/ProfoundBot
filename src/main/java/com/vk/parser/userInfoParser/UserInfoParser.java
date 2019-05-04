package com.vk.parser.userInfoParser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoParser {

    @SerializedName("response")
    private List<Response> responses;

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }
}