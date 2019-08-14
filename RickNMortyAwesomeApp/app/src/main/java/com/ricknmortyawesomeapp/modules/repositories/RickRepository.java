package com.ricknmortyawesomeapp.modules.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import com.ricknmortyawesomeapp.modules.models.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton pattern
 */
public class RickRepository {

    private static RickRepository instance;
    private List<Result> episodes = new ArrayList<>();
    private List<Result> characters = new ArrayList<>();
    private List<Result> dead = new ArrayList<>();
    private List<Result> alive = new ArrayList<>();
    private int selectedEpisode;
    private int selectedCharacter;
    private HashMap<String, Bitmap> photoThumbnails = new HashMap<>();

    public static RickRepository getInstance(){
        if(instance == null){
            instance = new RickRepository();
        }
        return instance;
    }

    public void setSelectedCharacter(int selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    public List<Result> getDead() {
        return dead;
    }

    public List<Result> getAlive() {
        return alive;
    }

    public int getSelectedEpisode() {
        return selectedEpisode;
    }

    public List<Result> getEpisodesModel() {
        return episodes;
    }

    public List<Result> getCharactersList() {
        return characters;
    }

    public void setPhotoThumbnails(String id, Bitmap image) {
        this.photoThumbnails.put(id, image);
    }

    public HashMap<String, Bitmap> getPhotoThumbnails() {
        return photoThumbnails;
    }

    public void setSelectedEpisode(int selectedEpisode) {
        this.selectedEpisode = selectedEpisode;
    }


    public void appendDataSet(List<Result> dataSet) {
        if (this.episodes == null) {
            this.episodes = dataSet;
        } else {
            this.episodes.addAll(dataSet);
        }
    }

    public void setCharcters(List<Result> dataModel) {
        if (this.characters == null) {
            this.characters = dataModel;
        } else {
            this.characters.addAll(dataModel);
        }
    }

    private MutableLiveData<List<Result>> getMutableLiveData(List<Result> list){
        MutableLiveData<List<Result>> episodesNamesMutableLiveData = new MutableLiveData<>();
        episodesNamesMutableLiveData.setValue(list);

        return episodesNamesMutableLiveData;
    }

    public MutableLiveData<List<Result>> getEpisodesNames(){
        return getMutableLiveData(episodes);
    }

    public MutableLiveData<List<Result>> getCharacters(){
        return getMutableLiveData(characters);
    }

    public MutableLiveData<List<Result>> getLiveCharacters(){
        return getMutableLiveData(alive);
    }

    public MutableLiveData<List<Result>> getDeadCharacters(){
        return getMutableLiveData(dead);
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
                Result character = characters.get(index - 1);
                if (character.status.equals("Alive")) {
                    alive.add(character);
                } else {
                    dead.add(character);
                }
            }
        }
    }

    public Result getSelectedCharacter(){
        for (Result result: characters){
            if (result.id == selectedCharacter){
                return result;
            }
        }
        return null;
    }

    public void toggleCharacterStatus(){
        Result resultCharacter = characters.get(selectedCharacter-1);
        resultCharacter.status = resultCharacter.status.equals("Alive")?"Dead":"Alive";
        seperateDeadAndAlive();
    }

    public String getSelectedEpisodeTitle(){
        return episodes.get(selectedEpisode).name;
    }

    public Bitmap getImage(final String url){
        return photoThumbnails.get(url);
    }
}











