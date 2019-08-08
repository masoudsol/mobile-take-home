package com.ricknmortyawesomeapp.modules.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.adapters.EpisodeRecycleViewAdapter;
import com.ricknmortyawesomeapp.modules.adapters.MainRecycleViewAdapter;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.viewmodels.EpisodeViewModel;
import com.ricknmortyawesomeapp.modules.viewmodels.MainViewModel;

import java.util.List;

public class EpisodeActivity extends AppCompatActivity {
    private static final String TAG = "EpisodeActivity";
    public EpisodeViewModel episodeViewModel;
    private EpisodeRecycleViewAdapter episodeRecycleViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode);

//        setTitle();

        episodeViewModel = ViewModelProviders.of(this).get(EpisodeViewModel.class);

        episodeViewModel.getCharactersMutableLiveData().observe(this, new Observer<List<ResultCharacters>>() {
            @Override
            public void onChanged(@Nullable List<ResultCharacters> dataModel) {
//                episodeRecycleViewAdapter.setupData();
                episodeRecycleViewAdapter.notifyDataSetChanged();
            }
        });

        episodeRecycleViewAdapter = new EpisodeRecycleViewAdapter(episodeViewModel.getEpisodesNamesMutableLiveData().getValue(), episodeViewModel.getCharactersMutableLiveData().getValue(),episodeViewModel.getSelectedEpisode(),this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(episodeRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
