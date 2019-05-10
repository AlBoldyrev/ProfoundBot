package com.vk.entities;

import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "state")
public class State {

    @Id
    @GeneratedValue
    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_info_sent")
    private boolean isInfoSent;

    @ManyToOne
    @JoinColumn(name = "audio")
    private Audio audio;

    public State(){}

    public State(Integer userId, boolean isInfoSent, Audio audio) {
        this.userId = userId;
        this.isInfoSent = isInfoSent;
        this.audio = audio;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isInfoSent() {
        return isInfoSent;
    }

    public void setInfoSent(boolean infoSent) {
        isInfoSent = infoSent;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return isInfoSent == state.isInfoSent &&
                Objects.equals(stateId, state.stateId) &&
                Objects.equals(userId, state.userId) &&
                Objects.equals(audio, state.audio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateId, userId, isInfoSent, audio);
    }
}
