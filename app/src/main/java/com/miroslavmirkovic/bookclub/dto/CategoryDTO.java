package com.miroslavmirkovic.bookclub.dto;

public class CategoryDTO {

    private int idCategory;
    private String name;

    public CategoryDTO(int idCategory, String name) {
        this.idCategory = idCategory;
        this.name = name;
    }

    public CategoryDTO() {

    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
