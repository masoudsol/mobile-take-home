package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.repositories.RickRepository;

public class CharacterViewModel extends SuperViewModel {

    private RickRepository dataProvider = RickRepository.getInstance();

    public CharacterViewModel(@NonNull Application application) {
        super(application);
    }

    public Result getCharacter(){
        return dataProvider.getSelectedCharacter();
    }

    public void toggleCharacterStatus(){
        dataProvider.toggleCharacterStatus();
    }
}
