package com.miroslavmirkovic.bookclub.retrofit;

import com.miroslavmirkovic.bookclub.retrofit.pojo.Book;
import com.miroslavmirkovic.bookclub.retrofit.pojo.BookCategory;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Category;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Comment;
import com.miroslavmirkovic.bookclub.retrofit.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {

    // /
    @GET("/")
    Call<List<Book>> getAllBooks();

    @POST("/register")
    Call<User> register(@Body User user);

    @GET("/getUserByEmail/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    @GET("/getUserById/{idUser}")
    Call<User> getUserById(@Path("idUser") Integer idUser);

    @PUT("/updateUser/{idUser}")
    Call<User> updateUser(@Body User user, @Path("idUser") Integer idUser);

    @GET("/getCategoryIdByName/{name}")
    Call<Integer> getIdCategoryByName(@Path("name") String name);

    @GET("/getAllCategories")
    Call<List<Category>> getAllCategories();

    // Book
    @GET("/book/getAllComments/{idBook}")
    Call<List<Comment>> getAllComments(@Path("idBook") Integer idBook);

    @POST("/book/addComment/{idUser}/{idBook}")
    Call<Comment> addComment(@Body Comment comment, @Path("idUser") Integer idUser, @Path("idBook") Integer idBook);

    @POST("/book/addBook/{idUser}")
    Call<Book> addBook(@Body Book book, @Path("idUser") Integer idUser);

    @GET("/book/getBook/{idBook}")
    Call<Book> getBookByIdBook(@Path("idBook") Integer idBook);

    @POST("/book/addBookCategory/{idCategory}")
    Call<BookCategory> addBookCategory(@Body Book book, @Path("idCategory") Integer idCategory);

    @GET("/book/getBookCategories/{idBook}")
    Call<List<String>> getBookCategories(@Path("idBook") Integer idBook);

}
