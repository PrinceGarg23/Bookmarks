package com.example.bookmarks;

import java.util.ArrayList;

public class ReadWriteUserDetails {
    public String name, email, image;
    public ArrayList<Saves> bookmarks;

    public ReadWriteUserDetails(String name, String email, ArrayList<Saves> bookmarks, String image) {
        this.name = name;
        this.email = email;
        this.bookmarks = bookmarks;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Saves> getBookmark() {
        return bookmarks;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setBookmarks(ArrayList<Saves> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
