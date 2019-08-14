package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;
import com.ricknmortyawesomeapp.services.APIServices;

import java.util.List;

public class EpisodeViewModel extends SuperViewModel {
    private static final String TAG = "EpisodeViewModel";

    private MutableLiveData<List<Result>> liveCharactersMutableLiveData;
    private MutableLiveData<List<Result>> deadCharactersMutableLiveData;
    public interface ImageDownloadListener {
        void onEvent(Bitmap bitmap, Exception error);   //method, which can have parameters
    }
    private RickRepository dataProvider = RickRepository.getInstance();

    public EpisodeViewModel(@NonNull Application application) {
        super(application);

        liveCharactersMutableLiveData = dataProvider.getLiveCharacters();
        deadCharactersMutableLiveData = dataProvider.getDeadCharacters();

        apiServices = new APIServices(application);
        if (dataProvider.getCharactersList().isEmpty()) {
            apiServices.fetchCharacters(new APIServices.CompletionListener() {
                @Override
                public void onCompletion(Boolean success, Exception error) {
                    if (success) {
                        dataProvider.seperateDeadAndAlive();
                        liveCharactersMutableLiveData.postValue(dataProvider.getAlive());
                        liveCharactersMutableLiveData.postValue(dataProvider.getDead());
                    } else {
                        Log.d(TAG, "onCompletion error: "+error);
                    }

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

    public MutableLiveData<List<Result>> getLiveCharactersMutableLiveData() {
        return liveCharactersMutableLiveData;
    }

    public MutableLiveData<List<Result>> getDeadCharactersMutableLiveData() {
        return deadCharactersMutableLiveData;
    }
}
