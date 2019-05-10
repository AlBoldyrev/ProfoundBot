package com.vk.entities;

import javax.persistence.*;

@Entity
@Table(name = "audios")
public class Audio {

    @Id
    @GeneratedValue
    @Column(name = "audio_id")
    private int audioId;

    @Column(name = "audio_name")
    private String audioName;

    //todo rename
    @Column(name = "audio_executor")
    private String audioArtist;

    @Column(name = "audio_title")
    private String audioTitle;

    @Column(name = "audio_vkid")
    private Integer audioVKID;

    @Column(name = "audio_owner_id")
    private Integer audioOwnerId;

    public Audio(String audioArtist, String audioTitle, Integer audioVKID, Integer audioOwnerId) {
        this.audioArtist = audioArtist;
        this.audioTitle = audioTitle;
        this.audioVKID = audioVKID;
        this.audioOwnerId = audioOwnerId;
    }

    public Audio(){}

    public String getAudioArtist() {
        return audioArtist;
    }

    public Integer getAudioVKID() {
        return audioVKID;
    }

    public void setAudioVKID(Integer audioVKID) {
        this.audioVKID = audioVKID;
    }

    public Integer getAudioOwnerId() {
        return audioOwnerId;
    }

    public void setAudioOwnerId(Integer audioOwnerId) {
        this.audioOwnerId = audioOwnerId;
    }

    public String getAudioExecutor() {
        return audioArtist;
    }

    public void setAudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Audio audio = (Audio) o;

        if (audioId != audio.audioId) return false;
        return audioName != null ? audioName.equals(audio.audioName) : audio.audioName == null;

    }

    @Override
    public int hashCode() {
        int result = audioId;
        result = 31 * result + (audioName != null ? audioName.hashCode() : 0);
        return result;
    }
}

