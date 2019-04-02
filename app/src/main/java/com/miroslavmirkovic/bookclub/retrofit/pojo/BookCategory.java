package com.miroslavmirkovic.bookclub.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookCategory {

    @SerializedName("idBookCategory")
    @Expose
    private int idBookCategory;
    @SerializedName("book")
    @Expose
    private Book book;
    @SerializedName("category")
    @Expose
    private Category category;

    public int getIdBookCategory() {
        return idBookCategory;
    }

    public void setIdBookCategory(int idBookCategory) {
        this.idBookCategory = idBookCategory;
    }

    public Book getBook() { return book; }

    public void setBook(Book book) { this.book = book; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }
}
