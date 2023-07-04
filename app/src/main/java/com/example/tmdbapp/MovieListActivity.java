package com.example.tmdbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tmdbapp.adapters.MovieRecyclerView;
import com.example.tmdbapp.adapters.OnMovieListener;
import com.example.tmdbapp.models.MovieModel;
import com.example.tmdbapp.viewmodel.MovieListViewModel;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;
    private MovieListViewModel movieListViewModel;

    private boolean showCategory = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetupSearchView();
        SetupCategoryButtons();

        recyclerView = findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObserveCategory();

        movieListViewModel.searchCategory(1,"popular");

    }


    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                        movieRecyclerAdapter.setmMovies(movieModels);
                }
            }
        });
    }
    private void ObserveCategory(){
        movieListViewModel.getCategory().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    movieRecyclerAdapter.setmMovies(movieModels);
                }
            }
        });
    }


    private void ConfigureRecyclerView(){
        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                        movieListViewModel.searchNextPage(showCategory);
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    private void SetupCategoryButtons() {
        final Button btn1 = findViewById(R.id.btnPopular);
        final Button btn2 = findViewById(R.id.btnTopRated);
        final Button btn3 = findViewById(R.id.btnUpcoming);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = true;
                movieListViewModel.searchCategory(
                        1,
                        "popular");
                recyclerView.scrollToPosition(0);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = true;
                movieListViewModel.searchCategory(
                        1,
                        "top_rated");
                recyclerView.scrollToPosition(0);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = true;
                movieListViewModel.searchCategory(
                        1,
                        "upcoming");
                recyclerView.scrollToPosition(0);
            }
        });

    }

    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = false;
            }
        });

    }

//    private void GetRetrofitResponse() {
//        MovieApi movieApi = MovieService.getMovieApi();
//
//        Call<MovieSearchResponse> responseCall = movieApi
//                .searchMovie(
//                        Credentials.API_KEY,
//                        "Action",
//                        1);
//
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if(response.code() == 200){
//                    Log.v("Tag","the response" + response.body().toString());
//
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//
//                    for(MovieModel movie: movies){
//                        Log.v("Tag", "The release date " + movie.getRelease_date());
//                    }
//                }else{
//                    try{
//                        Log.v("Tag", "Error" + response.errorBody().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    private void GetRetrofitResponseAccordingToID(){
//        MovieApi movieApi = MovieService.getMovieApi();
//        Call<MovieModel> responseCall = movieApi
//                .getMovie(550,
//                        Credentials.API_KEY);
//
//        responseCall.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if(response.code() == 200){
//                    MovieModel movie = response.body();
//                    Log.v("Tag", "The response " + movie.getTitle());
//                }else{
//                    try{
//                        Log.v("Tag", "Error "+response.errorBody().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//
//            }
//        });
//    }
}