package com.example.tmdbapp.utils;

import com.example.tmdbapp.models.MovieModel;
import com.example.tmdbapp.responses.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("/3/movie/{category}?")
    Call<MovieSearchResponse> getCategory(
            @Path("category") String category,
            @Query("api_key") String key,
            @Query("page") int page,
            @Query("language") String language
    );


    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );


}
