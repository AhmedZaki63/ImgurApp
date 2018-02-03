package com.example.ahmed.imgurapp.Models;


public class AlbumResponse {
    private Photo data;
    private Boolean success;
    private Integer status;

    public Photo getData() {
        return data;
    }

    public void setData(Photo data) {
        this.data = data;
    }
}
