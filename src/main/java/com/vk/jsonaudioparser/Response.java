package com.vk.jsonaudioparser;

import java.util.List;

public class Response {

    int count;
    List<Item> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
