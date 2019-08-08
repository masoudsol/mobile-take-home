package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;
import com.ricknmortyawesomeapp.services.APIServices;

import java.util.List;

public class EpisodeViewModel extends SuperViewModel {
    private MutableLiveData<List<ResultCharacters>> liveCharactersMutableLiveData;
    private MutableLiveData<List<ResultCharacters>> deadCharactersMutableLiveData;
    public interface ImageDownloadListener {
        void onEvent(Bitmap bitmap, Exception error);   //method, which can have parameters
    }
    private RickRepository dataProvider = RickRepository.getInstance();

    public EpisodeViewModel(@NonNull Application application) {
        super(application);

        liveCharactersMutableLiveData = dataProvider.getLiveCharacters();
        deadCharactersMutableLiveData = dataProvider.getDeadCharacters();

        apiServices = new APIServices(application);
        if (dataProvider.getCharactersList().size() < 1) {
            apiServices.fetchAllCharacters("https://rickandmortyapi.com/api/character/", new APIServices.CompletionListener() {
                @Override
                public void onCompletion(Boolean success, Exception error) {
                    dataProvider.seperateDeadAndAlive();
                    liveCharactersMutableLiveData.postValue(dataProvider.getAlive());
                    liveCharactersMutableLiveData.postValue(dataProvider.getDead());
                }
            });
        }
    }



    public String getSelectedEpisodeTitle(){
        return dataProvider.getSelectedEpisodeTitle();
    }

    public void setSelectedCharacter(int charID){
        dataProvider.setSelectedCharacter(charID);
    }

    public MutableLiveData<List<ResultCharacters>> getLiveCharactersMutableLiveData() {
        return liveCharactersMutableLiveData;
    }

    public MutableLiveData<List<ResultCharacters>> getDeadCharactersMutableLiveData() {
        return deadCharactersMutableLiveData;
    }
}
