package com.vk.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_vkid")
    private Integer userVkId;

    @Column(name = "user_name")
    private String userName;

    // ------

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserVkId() {
        return userVkId;
    }

    public void setUserVkId(Integer userVkId) {
        this.userVkId = userVkId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User that = (User) o;

        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return 31 + (userId != null ? userId.hashCode() : 0);
    }

}
