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

