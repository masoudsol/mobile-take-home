package com.ricknmortyawesomeapp.modules.views;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.viewmodels.CharacterViewModel;
import com.squareup.picasso.Picasso;


public class CharacterActivity extends AppCompatActivity {

    private static final String TAG = "EpisodeActivity";
    public CharacterViewModel characterViewModel;
    private Button button;
    private  ResultCharacters character;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_character);

        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);

        character = characterViewModel.getCharacter();
        button = findViewById(R.id.character_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCharacterStatus();
                character = characterViewModel.getCharacter();

                refreshPage();
            }
        });
        refreshPage();
    }

    private void toggleCharacterStatus(){
        characterViewModel.toggleCharacterStatus();
    }

    private void refreshPage(){
        setTitle(character.name);
        Picasso.with(this).load(character.image).into((ImageView) findViewById(R.id.character_image));

        ((TextView)findViewById(R.id.character_status)).setText(character.status);
        ((TextView)findViewById(R.id.character_gender)).setText(character.gender);
        ((TextView)findViewById(R.id.character_location)).setText(character.location.name);
        ((TextView)findViewById(R.id.character_origin)).setText(character.origin.name);
        ((TextView)findViewById(R.id.character_species)).setText(character.species);

        button.setText(isAlive(character.status)?"KILL":"REVIVE");
        button.setBackgroundColor(isAlive(character.status)? Color.BLUE: Color.RED);
    }

    private boolean isAlive(String status){
        return character.status.equals("Alive");
    }
}
