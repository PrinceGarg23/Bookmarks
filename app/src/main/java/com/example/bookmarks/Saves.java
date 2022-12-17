package com.example.bookmarks;

public class Saves {
    String heading, url, description, image;

    public Saves(String heading, String url, String description, String image) {
        this.heading = heading;
        this.url = url;
        this.description = description;
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
