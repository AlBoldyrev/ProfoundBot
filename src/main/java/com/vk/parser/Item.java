package com.vk.parser;

import java.util.List;

public class Item {

    private int id;
    private int from_id;
    private int owner_id;
    private int date;
    private int marked_as_ads;
    private String post_type;
    private String text;
    private int can_edit;
    private int created_by;
    private int can_delete;
    private int can_pin;
    private List<Attachment> attachments;
    private PostSource post_source;
    private Comment comments;
    private Like likes;
    private Repost reposts;
    private View views;
    private Message message;
    private int unread;
    private Conversation conversation;

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

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
