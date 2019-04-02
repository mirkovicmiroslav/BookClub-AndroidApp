package com.miroslavmirkovic.bookclub.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("idComment")
    @Expose
    private int idComment;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("book")
    @Expose
    private Book book;
    @SerializedName("user")
    @Expose
    private User user;

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}