package com.example.ahmed.imgurapp.Models;

import java.util.ArrayList;

public class PhotoResponse {
    private ArrayList<Photo> data = null;

    public ArrayList<Photo> getData() {
        return data;
    }

    public void setData(ArrayList<Photo> data) {
        this.data = data;
    }
}
