package com.planer.catthemeplaner.model;

public class StoreList {
    int image;
    String content;
    String description;

    public StoreList(int image, String content, String description) {
        this.image = image;
        this.content = content;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
