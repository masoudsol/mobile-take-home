package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.services.APIServices;

import java.util.List;

public class MainViewModel extends SuperViewModel {

    private MutableLiveData<List<Result>> episodesNamesMutableLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        episodesNamesMutableLiveData = dataProvider.getEpisodesNames();

        APIServices apiServices = new APIServices(application);
        apiServices.fetchAllEpisodes("https://rickandmortyapi.com/api/episode/", new APIServices.CompletionListener() {

            @Override
            public void onCompletion(Boolean success, Exception error) {
                    episodesNamesMutableLiveData.postValue( dataProvider.getEpisodesModel());
            }
        });
    }

    public LiveData<List<Result>> getEpisodesNamesMutableLiveData() {
        return episodesNamesMutableLiveData;
    }

    public void setEpisodeSelected(int i){
        dataProvider.setSelectedEpisode(i);
    }

    public void updateDeadAliveCharacters(){
        dataProvider.seperateDeadAndAlive();
    }
}
