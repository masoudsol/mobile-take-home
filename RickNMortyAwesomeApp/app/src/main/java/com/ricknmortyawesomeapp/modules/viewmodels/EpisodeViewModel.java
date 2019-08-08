package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.models.CharModel;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;
import com.ricknmortyawesomeapp.services.APIServices;

import java.util.List;

public class EpisodeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Result>> episodesNamesMutableLiveData;
    private MutableLiveData<List<ResultCharacters>> charactersMutableLiveData;

    private APIServices apiServices;
    private RickRepository dataProvider = RickRepository.getInstance();

    public EpisodeViewModel(@NonNull Application application) {
        super(application);

        episodesNamesMutableLiveData = dataProvider.getEpisodesNames();
        charactersMutableLiveData = dataProvider.getCharacters();
        apiServices = new APIServices(application);
        apiServices.fetchAllCharacters("https://rickandmortyapi.com/api/character/", new APIServices.CompletionListener() {
            @Override
            public void onCompletion(Boolean success, Exception error) {
                charactersMutableLiveData.postValue(dataProvider.getCharactersList());
            }
        });
    }

    public MutableLiveData<List<Result>> getEpisodesNamesMutableLiveData() {
        return episodesNamesMutableLiveData;
    }

    public MutableLiveData<List<ResultCharacters>> getCharactersMutableLiveData() {
        return charactersMutableLiveData;
    }

    public int getSelectedEpisode(){
        return dataProvider.getSelectedEpisode();
    }
}
