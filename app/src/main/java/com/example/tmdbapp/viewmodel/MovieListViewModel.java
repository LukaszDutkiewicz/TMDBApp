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
    public LiveData<List<MovieModel>> getCategory(){
        return movieRepository.getCategory();
    }

    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }
    public void searchCategory(int pageNumber, String category){
        movieRepository.searchCategory(pageNumber, category);
    }

    public void searchNextPage(boolean isCategory){
        movieRepository.searchNextPage(isCategory);
    }

}
