package com.example.tmdbapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tmdbapp.models.MovieModel;
import com.example.tmdbapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPopular(){
        return movieRepository.getPopular();
    }

    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }
    public void searchPopular(int pageNumber){
        movieRepository.searchPopular(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }

}
