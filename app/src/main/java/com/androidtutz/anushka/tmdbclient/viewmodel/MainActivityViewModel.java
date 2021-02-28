package com.androidtutz.anushka.tmdbclient.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.androidtutz.anushka.tmdbclient.model.Movie;
import com.androidtutz.anushka.tmdbclient.model.MovieDataSource;
import com.androidtutz.anushka.tmdbclient.model.MovieDataSourceFactory;
import com.androidtutz.anushka.tmdbclient.model.MovieRepository;
import com.androidtutz.anushka.tmdbclient.service.MovieDataService;
import com.androidtutz.anushka.tmdbclient.service.RetrofitInstance;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;

    Executor executor;
    androidx.lifecycle.LiveData<PagedList<Movie>> moviePageList ;
    private MutableLiveData<MovieDataSource> movieDataLiveData ;
//    ViewModel For Pagiging
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieRepository=new MovieRepository(application);

        // get moviedata source , we need to instance on movie servervie

        MovieDataService mService = RetrofitInstance.getService();

        // factory and paging library

        MovieDataSourceFactory factory = new MovieDataSourceFactory(mService, application);

        movieDataLiveData=factory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPageSize(20)
                .setInitialLoadSizeHint(10)
                .setPrefetchDistance(4)
                .build();

        executor= Executors.newFixedThreadPool(5);

        // live data
        moviePageList= (new LivePagedListBuilder<Long, Movie>(factory , config))
                .setFetchExecutor(executor).
                        build();
    }

    public androidx.lifecycle.LiveData<PagedList<Movie>> getMoviePageList() {
        return moviePageList;
    }
    public MutableLiveData<MovieDataSource> getMovieDataLiveData() {
        return movieDataLiveData;
    }
    public LiveData<List<Movie>> getAllMovies(){

        return movieRepository.getMutableLiveData();
    }


}
