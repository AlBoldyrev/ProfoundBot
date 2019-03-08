package com.vk.model.message_reply;

public class Model {
    public String type;
    public InnerObject innerObject;
    public String group_id;
}

class InnerObject {
    public String state;
    public Integer from_id;
    public Integer to_id;

}