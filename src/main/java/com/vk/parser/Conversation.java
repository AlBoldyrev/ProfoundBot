package com.vk.parser;

import com.google.gson.annotations.SerializedName;

public class Conversation {

    private Peer peer;
    @SerializedName("in_read")
    private int inRead;
    @SerializedName("out_read")
    private int outRead;
    @SerializedName("last_message_id")
    private int lastMessageId;
    @SerializedName("unread_count")
    private int unreadCount;
    private boolean unanswered;
    @SerializedName("can_write")
    private CanWrite canWrite;

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public int getInRead() {
        return inRead;
    }

    public void setInRead(int inRead) {
        this.inRead = inRead;
    }

    public int getOutRead() {
        return outRead;
    }

    public void setOutRead(int outRead) {
        this.outRead = outRead;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isUnanswered() {
        return unanswered;
    }

    public void setUnanswered(boolean unanswered) {
        this.unanswered = unanswered;
    }

    public CanWrite getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(CanWrite canWrite) {
        this.canWrite = canWrite;
    }


}
