package com.androidtutz.anushka.tmdbclient.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.androidtutz.anushka.tmdbclient.service.MovieDataService;

public class MovieDataSourceFactory extends DataSource.Factory {
    private MovieDataSource movieDataSource;
    private MovieDataService movieDataService;
    private Application application;

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
    MutableLiveData<MovieDataSource> mutableLiveData;
    public MovieDataSourceFactory( MovieDataService movieDataService1, Application application) {
        this.movieDataService = movieDataService1;
        this.application = application;
        mutableLiveData =new MutableLiveData<MovieDataSource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource(movieDataService,application);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

}
