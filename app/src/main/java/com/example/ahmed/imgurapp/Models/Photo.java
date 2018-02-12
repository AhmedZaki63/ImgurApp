package com.example.ahmed.imgurapp.Models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Photo {
    private String id;
    private String title;
    private String description;
    private Integer datetime;
    private Integer views;
    private String link;
    private Integer ups;
    private Integer downs;
    private Integer points;
    private Integer score;
    private Boolean is_album;
    private List<Tag> tags = null;
    //only in album
    private String cover;
    private Integer cover_width;
    private Integer cover_height;
    private Integer images_count;
    private ArrayList<Photo> images = null;
    //only in image
    private String type;
    private Boolean animated;
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

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getUps() {
        return ups;
    }

    public void setUps(Integer ups) {
        this.ups = ups;
    }

    public Integer getDowns() {
        return downs;
    }

    public void setDowns(Integer downs) {
        this.downs = downs;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getIs_album() {
        return is_album;
    }

    public void setIs_album(Boolean is_album) {
        this.is_album = is_album;
    }

    public Integer getImages_count() {
        return images_count;
    }

    public void setImages_count(Integer images_count) {
        this.images_count = images_count;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Photo> getImages() {
        return images;
    }

    public void setImages(ArrayList<Photo> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAnimated() {
        return animated;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
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
