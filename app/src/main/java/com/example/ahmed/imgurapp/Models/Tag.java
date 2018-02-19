package com.example.ahmed.imgurapp.Models;

import org.parceler.Parcel;

@Parcel
public class Tag {
    private String name;
    private String backgroundHash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundHash() {
        return backgroundHash;
    }

    public void setBackgroundHash(String backgroundHash) {
        this.backgroundHash = backgroundHash;
    }
}
