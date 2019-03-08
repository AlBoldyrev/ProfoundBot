package com.vk.entities;


import org.hibernate.annotations.GeneratorType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "user_vk_id")
    private Integer userVkId;

    @Column(name = "message_value")
    private String messageValue;

    // ------

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserVkId() {
        return userVkId;
    }

    public void setUserVkId(Integer userId) {
        this.userVkId = userVkId;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public void setMessageValue(String messageValue) {
        this.messageValue = messageValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Message that = (Message) o;

        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return 31 + (messageId != null ? messageId.hashCode() : 0);
    }

}
