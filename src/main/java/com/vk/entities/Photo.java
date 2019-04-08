package com.vk.entities;

import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private int photoId;

    @Column(name ="photo_name")
    private String photoName;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (photoId != photo.photoId) return false;
        return photoName != null ? photoName.equals(photo.photoName) : photo.photoName == null;

    }

    @Override
    public int hashCode() {
        int result = photoId;
        result = 31 * result + (photoName != null ? photoName.hashCode() : 0);
        return result;
    }
}
