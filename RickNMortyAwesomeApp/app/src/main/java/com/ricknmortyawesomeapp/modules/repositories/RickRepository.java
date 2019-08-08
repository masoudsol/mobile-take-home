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
    private List<ResultCharacters> dead = new ArrayList<>();
    private List<ResultCharacters> alive = new ArrayList<>();
    private int selectedEpisode;
    private int selectedCharacter;

    public static RickRepository getInstance(){
        if(instance == null){
            instance = new RickRepository();
        }
        return instance;
    }

    public void setSelectedCharacter(int selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    public List<ResultCharacters> getDead() {
        return dead;
    }

    public List<ResultCharacters> getAlive() {
        return alive;
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

    public MutableLiveData<List<ResultCharacters>> getLiveCharacters(){
        MutableLiveData<List<ResultCharacters>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(alive);

        return episodesNamesMutableLiveData;
    }

    public MutableLiveData<List<ResultCharacters>> getDeadCharacters(){
        MutableLiveData<List<ResultCharacters>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(dead);

        return episodesNamesMutableLiveData;
    }

    public void setSelectedEpisode(int selectedEpisode) {
        this.selectedEpisode = selectedEpisode;
    }

    public void seperateDeadAndAlive(){
        List<Integer> charIDs = new ArrayList<>();
        for (String result : episodes.get(selectedEpisode).characters) {
            charIDs.add(Integer.valueOf(result.replace("https://rickandmortyapi.com/api/character/", "")));
        }
        if (characters.size() > charIDs.get(charIDs.size() - 1)) {
            alive.clear();
            dead.clear();

            for (int index : charIDs) {
                ResultCharacters character = characters.get(index - 1);
                if (character.status.equals("Alive")) {
                    alive.add(character);
                } else {
                    dead.add(character);
                }
            }
        }
    }

    public ResultCharacters getSelectedCharacter(){
        for (ResultCharacters result: characters){
            if (result.id == selectedCharacter){
                return result;
            }
        }
        return null;
    }

    public void toggleCharacterStatus(){
        ResultCharacters resultCharacter = characters.get(selectedCharacter-1);
        resultCharacter.status = resultCharacter.status.equals("Alive")?"Dead":"Alive";
        seperateDeadAndAlive();
    }

    public String getSelectedEpisodeTitle(){
        return episodes.get(selectedEpisode).name;
    }
}











