package com.androidtutz.anushka.tmdbclient.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.androidtutz.anushka.tmdbclient.R;
import com.androidtutz.anushka.tmdbclient.service.MovieDataService;
import com.androidtutz.anushka.tmdbclient.service.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDataSource  extends PageKeyedDataSource<Long, Movie> {

    MovieDataService movieDataService;

    Application application;

    public MovieDataSource(MovieDataService movieDataSource,
                           Application application) {
        this.movieDataService = movieDataSource;
        this.application=application;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull final LoadInitialCallback<Long, Movie> callback) {

        // first time data loading;
        movieDataService = RetrofitInstance.getService() ;

        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key), 1);

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                if(response.isSuccessful()) {
                    MovieDBResponse movieDBResponse = response.body();
                    if (movieDBResponse != null) {
                        ArrayList<Movie> moviews = (ArrayList<Movie>) movieDBResponse.getMovies();
                        callback.onResult(moviews, null, (long) 2);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
    }
    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, final  @NonNull LoadCallback<Long, Movie> callback) {
        movieDataService = RetrofitInstance.getService() ;
        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key), params.key);
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                if(response.isSuccessful()) {
                    MovieDBResponse movieDBResponse = response.body();
                    if(movieDBResponse!=null) {
                        ArrayList<Movie> moviews = (ArrayList<Movie>) movieDBResponse.getMovies();
                        callback.onResult(moviews, params.key + 1);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
            }
        });

    }
}
