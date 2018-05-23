package com.example.Laki.myapplication.backend;

public class Comment {

    private String commentId;
    private String comment;
    private String commentorName;
    private String commentedName;
    private String commentorAvatarUrl;
    private String commentedAvatarUrl;
    private String timePosted;
    private String commentedUUID;

    public Comment(){}



    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getCommentedAvatarUrl() {
        return commentedAvatarUrl;
    }

    public void setCommentedAvatarUrl(String commentedAvatarUrl) {
        this.commentedAvatarUrl = commentedAvatarUrl;
    }

    public String getCommentorAvatarUrl() {
        return commentorAvatarUrl;
    }

    public void setCommentorAvatarUrl(String commentorAvatarUrl) {
        this.commentorAvatarUrl = commentorAvatarUrl;
    }

    public String getCommentedName() {
        return commentedName;
    }

    public void setCommentedName(String commentedName) {
        this.commentedName = commentedName;
    }

    public String getCommentorName() {
        return commentorName;
    }

    public void setCommentorName(String commentorName) {
        this.commentorName = commentorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentedUUID() {
        return commentedUUID;
    }

    public void setCommentedUUID(String commentedUUID) {
        this.commentedUUID = commentedUUID;
    }
}
