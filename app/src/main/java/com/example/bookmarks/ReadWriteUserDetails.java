package com.example.bookmarks;

import java.util.ArrayList;

public class ReadWriteUserDetails {
    public String name, email;
    public ArrayList<Saves> bookmarks;

    public ReadWriteUserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Saves> getBookmarks() {
        return bookmarks;
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



}
