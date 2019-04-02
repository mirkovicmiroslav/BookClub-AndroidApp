package com.miroslavmirkovic.bookclub;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.miroslavmirkovic.bookclub.dao.BookDao;
import com.miroslavmirkovic.bookclub.entities.BookEntity;


@Database(entities = {BookEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao getBookDAO();
}
