package com.vk.parser;

public class Comment {

    private int count;
    private int can_post;
    private boolean groups_can_post;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCan_post() {
        return can_post;
    }

    public void setCan_post(int can_post) {
        this.can_post = can_post;
    }

    public boolean isGroups_can_post() {
        return groups_can_post;
    }

    public void setGroups_can_post(boolean groups_can_post) {
        this.groups_can_post = groups_can_post;
    }
}
