package com.ricknmortyawesomeapp.modules.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.ricknmortyawesomeapp.modules.models.CharModel;
import com.ricknmortyawesomeapp.modules.models.DataModel;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton pattern
 */
public class RickRepository {

    private static RickRepository instance;
    private List<Result> episodes = new ArrayList<>();
    private List<ResultCharacters> characters = new ArrayList<>();
    private int selectedEpisode;

    public static RickRepository getInstance(){
        if(instance == null){
            instance = new RickRepository();
        }
        return instance;
    }

    public int getSelectedEpisode() {
        return selectedEpisode;
    }

    public List<Result> getEpisodesModel() {
        return episodes;
    }

    public List<ResultCharacters> getCharactersList() {
        return characters;
    }

    public void appendDataSet(List<Result> dataSet) {
        if (this.episodes == null) {
            this.episodes = dataSet;
        } else {
            this.episodes.addAll(dataSet);
        }
    }

    public void setCharcters(List<ResultCharacters> dataModel) {
        if (this.characters == null) {
            this.characters = dataModel;
        } else {
            this.characters.addAll(dataModel);
        }
    }

    public MutableLiveData<List<Result>> getEpisodesNames(){
        MutableLiveData<List<Result>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(episodes);

        return episodesNamesMutableLiveData;
    }

    public MutableLiveData<List<ResultCharacters>> getCharacters(){
        MutableLiveData<List<ResultCharacters>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(characters);

        return episodesNamesMutableLiveData;
    }

    public void setSelectedEpisode(int selectedEpisode) {
        this.selectedEpisode = selectedEpisode;
    }
}











