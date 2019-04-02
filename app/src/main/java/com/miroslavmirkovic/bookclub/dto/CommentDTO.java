package com.miroslavmirkovic.bookclub.dto;

public class CommentDTO {

    private int idComment;
    private String content;
    private String dateTime;
    private int idBook;
    private int idUser;

    public CommentDTO(int idComment, String content, String dateTime, int idBook, int idUser) {
        this.idComment = idComment;
        this.content = content;
        this.dateTime = dateTime;
        this.idBook = idBook;
        this.idUser = idUser;
    }

    public CommentDTO(){

    }

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

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
