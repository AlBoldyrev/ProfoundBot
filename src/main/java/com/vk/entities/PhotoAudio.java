package com.vk.entities;

import javax.persistence.*;

@Entity
@Table(name = "photos_audios")
public class PhotoAudio {

    @Id
    @GeneratedValue
    @Column(name = "photo_audio_id")
    private int photoAudioId;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "audio_name")
    private String audioName;

    public int getPhotoAudioId() {
        return photoAudioId;
    }

    public void setPhotoAudioId(int photoId) {
        this.photoAudioId = photoId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
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

        PhotoAudio that = (PhotoAudio) o;

        if (photoAudioId != that.photoAudioId) return false;
        if (photoName != null ? !photoName.equals(that.photoName) : that.photoName != null) return false;
        return audioName != null ? audioName.equals(that.audioName) : that.audioName == null;

    }

    @Override
    public int hashCode() {
        int result = photoAudioId;
        result = 31 * result + (photoName != null ? photoName.hashCode() : 0);
        result = 31 * result + (audioName != null ? audioName.hashCode() : 0);
        return result;
    }
}
