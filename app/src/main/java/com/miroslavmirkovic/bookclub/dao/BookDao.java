package com.miroslavmirkovic.bookclub.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.miroslavmirkovic.bookclub.entities.BookEntity;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void addBook(BookEntity bookEntity);

    @Query("SELECT * FROM books")
    List<BookEntity> getAllBooks();

    @Query("SELECT * FROM books WHERE title LIKE :title")
    BookEntity getBookByTitle(String title);

}
