package com.example.tmdbapp.repositories;

import androidx.lifecycle.LiveData;

import com.example.tmdbapp.models.MovieModel;
import com.example.tmdbapp.requests.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private String mCategory;
    private int mPageNumber;

    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getCategory(){
        return movieApiClient.getCategory();
    }


    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(mQuery, mPageNumber);
    }

    public void searchCategory(int pageNumber, String category){
        mPageNumber = pageNumber;
        mCategory = category;
        movieApiClient.searchCategory(pageNumber, category);
    }

    public void searchNextPage(boolean isCategory){
        if(isCategory){
            searchCategory(mPageNumber+1, mCategory);
        }else{
            searchMovieApi(mQuery, mPageNumber + 1);
        }

    }
}
