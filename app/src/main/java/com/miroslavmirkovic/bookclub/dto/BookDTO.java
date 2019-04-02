package com.miroslavmirkovic.bookclub.dto;

public class BookDTO {

    private int idBook;
    private String author;
    private byte[] image;
    private String description;
    private String publisher;
    private String title;
    private int idUser;

    public BookDTO(int idBook, String author, byte[] image, String description, String publisher, String title, int idUser) {
        this.idBook = idBook;
        this.author = author;
        this.image = image;
        this.description = description;
        this.publisher = publisher;
        this.title = title;
        this.idUser = idUser;
    }

    public BookDTO() {

    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) { this.image = image; }

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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
