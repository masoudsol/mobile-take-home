package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;

public class CharacterViewModel extends AndroidViewModel {

    private RickRepository dataProvider = RickRepository.getInstance();


    public CharacterViewModel(@NonNull Application application) {
        super(application);
    }

    public ResultCharacters getCharacter(){
        return dataProvider.getSelectedCharacter();
    }

    public void toggleCharacterStatus(){
        dataProvider.toggleCharacterStatus();
    }
}
