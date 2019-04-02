package com.miroslavmirkovic.bookclub.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("idBook")
    @Expose
    private int idBook;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("bookImage")
    @Expose
    private byte[] bookImage;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user")
    @Expose
    private User user;

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public byte[] getBookImage() {
        return bookImage;
    }

    public void setBookImage(byte[] image) {
        this.bookImage = bookImage;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}