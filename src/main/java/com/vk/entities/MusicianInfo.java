package com.vk.entities;

import javax.persistence.*;

@Entity
@Table(name = "musician_info")
public class MusicianInfo {

    @Id
    @GeneratedValue
    @Column(name = "musician_info_id")
    private Integer musicianInfoId;

    @Column(name = "musician_name")
    private String musicianName;

    @Column(name = "number_of_clicks")
    private Integer numberOfClicks;

    public Integer getMusicianInfoId() {
        return musicianInfoId;
    }

    public void setMusicianInfoId(Integer musicianInfoId) {
        this.musicianInfoId = musicianInfoId;
    }

    public String getMusicianName() {
        return musicianName;
    }

    public void setMusicianName(String musicianName) {
        this.musicianName = musicianName;
    }

    public Integer getNumberOfClicks() {
        return numberOfClicks;
    }

    public void setNumberOfClicks(Integer numberOfClicks) {
        this.numberOfClicks = numberOfClicks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicianInfo that = (MusicianInfo) o;

        if (musicianInfoId != null ? !musicianInfoId.equals(that.musicianInfoId) : that.musicianInfoId != null)
            return false;
        if (musicianName != null ? !musicianName.equals(that.musicianName) : that.musicianName != null) return false;
        return numberOfClicks != null ? numberOfClicks.equals(that.numberOfClicks) : that.numberOfClicks == null;

    }

    @Override
    public int hashCode() {
        int result = musicianInfoId != null ? musicianInfoId.hashCode() : 0;
        result = 31 * result + (musicianName != null ? musicianName.hashCode() : 0);
        result = 31 * result + (numberOfClicks != null ? numberOfClicks.hashCode() : 0);
        return result;
    }
}
