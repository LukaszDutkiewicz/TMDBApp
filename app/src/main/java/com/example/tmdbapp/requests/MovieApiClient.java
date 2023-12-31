package com.example.tmdbapp.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmdbapp.AppExecutors;
import com.example.tmdbapp.models.MovieModel;
import com.example.tmdbapp.responses.MovieSearchResponse;
import com.example.tmdbapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //Live Data for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Live Data for selected category
    private MutableLiveData<List<MovieModel>> mCategory;
    private RetrieveCategoryRunnable retrieveCategoryRunnable;



    public static MovieApiClient getInstance(){
        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mCategory = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }

    public LiveData<List<MovieModel>> getCategory(){
        return mCategory;
    }

    public void searchMoviesApi(String query, int pageNumber) {

        if(retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future mHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                mHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchCategory(int pageNumber, String category) {

        if(retrieveCategoryRunnable != null){
            retrieveCategoryRunnable = null;
        }

        retrieveCategoryRunnable = new RetrieveCategoryRunnable(pageNumber, category);

        final Future mHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveCategoryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                mHandler2.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getMovies(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200 && response.body() != null){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        mMovies.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }else{
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error " + error);
                    mMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return MovieService.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber,
                    Credentials.LANG
            );
        }
        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveCategoryRunnable implements Runnable{

        private int pageNumber;
        private String category;
        boolean cancelRequest;

        public RetrieveCategoryRunnable(int pageNumber, String category) {
            this.pageNumber = pageNumber;
            this.category = category;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response2 = getCategory(pageNumber, category).execute();
                if(cancelRequest){
                    return;
                }
                if(response2.code() == 200 && response2.body() != null){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        mCategory.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mCategory.getValue();
                        currentMovies.addAll(list);
                        mCategory.postValue(currentMovies);
                    }
                }else{
                    String error = response2.errorBody().toString();
                    Log.v("Tag", "Error " + error);
                    mCategory.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mCategory.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getCategory(int pageNumber, String category){
            return MovieService.getMovieApi().getCategory(
                    category,
                    Credentials.API_KEY,
                    pageNumber,
                    Credentials.LANG
            );
        }
        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}
