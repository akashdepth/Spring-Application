package com.bs.social.models;

import com.bs.social.models.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class ContentSection {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String title;
    private Long userId;
    private long noOfShares;
    private long noOfLikes;
    private long noOfComment;
    private String language;
    private String contentType;
    private long timestamp;
    private String url;
    private String lightWeightUrl;
    private String about;
    private Boolean likeStatus;

    public Boolean getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(Boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public ContentSection(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(long noOfShares) {
        this.noOfShares = noOfShares;
    }

    public long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public long getNoOfComment() {
        return noOfComment;
    }

    public void setNoOfComment(long noOfComment) {
        this.noOfComment = noOfComment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLightWeightUrl() {
        return lightWeightUrl;
    }

    public void setLightWeightUrl(String lightWeightUrl) {
        this.lightWeightUrl = lightWeightUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
