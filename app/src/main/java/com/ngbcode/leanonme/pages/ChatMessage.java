package com.ngbcode.leanonme.pages;

/**
 * Created by Nate on 4/21/2017.
 */

public class ChatMessage {

    private String id;
    private String text;
    private String name;
    private String photoUrl;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
