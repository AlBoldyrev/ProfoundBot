package com.vk.jsonaudioparser;

import java.util.List;

public class Item {

    int id;
    int from_id;
    int owner_id;
    int date;
    int marked_as_ads;
    String post_type;
    String text;
    int can_edit;
    int created_by;
    int can_delete;
    int can_pin;
    List<Attachment> attachments;
    PostSource post_source;
    Comment comments;
    Like likes;
    Repost reposts;
    View views;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMarked_as_ads() {
        return marked_as_ads;
    }

    public void setMarked_as_ads(int marked_as_ads) {
        this.marked_as_ads = marked_as_ads;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCan_edit() {
        return can_edit;
    }

    public void setCan_edit(int can_edit) {
        this.can_edit = can_edit;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(int can_delete) {
        this.can_delete = can_delete;
    }

    public int getCan_pin() {
        return can_pin;
    }

    public void setCan_pin(int can_pin) {
        this.can_pin = can_pin;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public PostSource getPost_source() {
        return post_source;
    }

    public void setPost_source(PostSource post_source) {
        this.post_source = post_source;
    }

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

    public Like getLikes() {
        return likes;
    }

    public void setLikes(Like likes) {
        this.likes = likes;
    }

    public Repost getReposts() {
        return reposts;
    }

    public void setReposts(Repost reposts) {
        this.reposts = reposts;
    }

    public View getViews() {
        return views;
    }

    public void setViews(View views) {
        this.views = views;
    }
}
