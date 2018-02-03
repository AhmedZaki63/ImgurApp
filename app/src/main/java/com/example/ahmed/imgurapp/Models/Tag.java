package com.example.ahmed.imgurapp.Models;

import org.parceler.Parcel;

@Parcel
public class Tag {
    private String name;
    private String displayName;
    private Integer followers;
    private Integer totalItems;
    private Boolean following;
    private String backgroundHash;
    private String accent;
    private Boolean backgroundIsAnimated;
    private Boolean thumbnailIsAnimated;
    private Boolean isPromoted;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public String getBackgroundHash() {
        return backgroundHash;
    }

    public void setBackgroundHash(String backgroundHash) {
        this.backgroundHash = backgroundHash;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public Boolean getBackgroundIsAnimated() {
        return backgroundIsAnimated;
    }

    public void setBackgroundIsAnimated(Boolean backgroundIsAnimated) {
        this.backgroundIsAnimated = backgroundIsAnimated;
    }

    public Boolean getThumbnailIsAnimated() {
        return thumbnailIsAnimated;
    }

    public void setThumbnailIsAnimated(Boolean thumbnailIsAnimated) {
        this.thumbnailIsAnimated = thumbnailIsAnimated;
    }

    public Boolean getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(Boolean isPromoted) {
        this.isPromoted = isPromoted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
