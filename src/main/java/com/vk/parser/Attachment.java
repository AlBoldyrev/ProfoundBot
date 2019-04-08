package com.vk.parser;

public class Attachment {

    private String type;
    private Audio audio;
    private Photo photo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
