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
import android.view.View;
import android.widget.Button;

import com.example.tmdbapp.adapters.MovieRecyclerView;
import com.example.tmdbapp.adapters.OnMovieListener;
import com.example.tmdbapp.databinding.ActivityMovieListBinding;
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
        //setContentView(R.layout.activity_movie_list);

        ActivityMovieListBinding binding = ActivityMovieListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        SetupSearchView(binding);
        SetupCategoryButtons(binding);

        //recyclerView = findViewById(R.id.recyclerView);
        recyclerView = binding.recyclerView;
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

    private void SetupCategoryButtons(ActivityMovieListBinding binding) {
//        final Button btn1 = findViewById(R.id.btnPopular);
//        final Button btn2 = findViewById(R.id.btnTopRated);
//        final Button btn3 = findViewById(R.id.btnUpcoming);
        binding.btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = true;
                movieListViewModel.searchCategory(
                        1,
                        "popular");
                recyclerView.scrollToPosition(0);
            }
        });

        binding.btnTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = true;
                movieListViewModel.searchCategory(
                        1,
                        "top_rated");
                recyclerView.scrollToPosition(0);
            }
        });

        binding.btnUpcoming.setOnClickListener(new View.OnClickListener() {
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

    private void SetupSearchView(ActivityMovieListBinding binding) {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        binding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory = false;
            }
        });

    }

}