package com.example.ahmed.imgurapp.Models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Photo {
    private String id;
    private String title;
    private String description;
    private Boolean is_album;
    private List<Tag> tags = null;
    //only in album
    private String cover;
    private Integer cover_width;
    private Integer cover_height;
    private Integer images_count;
    private ArrayList<Photo> images = null;
    //only in image
    private Integer width;
    private Integer height;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_album() {
        return is_album;
    }

    public void setIs_album(Boolean is_album) {
        this.is_album = is_album;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getCover() {
        if (is_album != null && is_album)
            return cover;
        else
            return id;
    }

    public void setCover(String cover) {
        if (is_album != null && is_album)
            this.id = cover;
        else
            this.cover = cover;
    }

    public Integer getImages_count() {
        return images_count;
    }

    public void setImages_count(Integer images_count) {
        this.images_count = images_count;
    }

    public ArrayList<Photo> getImages() {
        return images;
    }

    public void setImages(ArrayList<Photo> images) {
        this.images = images;
    }

    public Integer getWidth() {
        if (is_album != null && is_album)
            return cover_width;
        else
            return width;
    }

    public void setWidth(Integer width) {
        if (is_album != null && is_album)
            this.cover_width = width;
        else
            this.width = width;
    }

    public Integer getHeight() {
        if (is_album != null && is_album)
            return cover_height;
        else
            return height;
    }

    public void setHeight(Integer height) {
        if (is_album != null && is_album)
            this.cover_height = height;
        else
            this.height = height;
    }
}
