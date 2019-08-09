package com.ricknmortyawesomeapp.modules.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.adapters.EpisodeRecycleViewAdapter;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.viewmodels.EpisodeViewModel;

import java.util.List;

public class EpisodeActivity extends AppCompatActivity {
    private static final String TAG = "EpisodeActivity";
    public EpisodeViewModel episodeViewModel;
    private EpisodeRecycleViewAdapter episodeRecycleViewAdapter;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_episode);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        episodeViewModel = ViewModelProviders.of(this).get(EpisodeViewModel.class);

        episodeViewModel.getLiveCharactersMutableLiveData().observe(this, new Observer<List<ResultCharacters>>() {
            @Override
            public void onChanged(@Nullable List<ResultCharacters> dataModel) {
                if (dataModel != null && dataModel.size()>0) {
                    mProgressBar.setVisibility(View.GONE);

                    episodeRecycleViewAdapter.notifyDataSetChanged();
                }
            }
        });

        setTitle(episodeViewModel.getSelectedEpisodeTitle());

        episodeRecycleViewAdapter = new EpisodeRecycleViewAdapter(episodeViewModel.getLiveCharactersMutableLiveData().getValue(), episodeViewModel.getDeadCharactersMutableLiveData().getValue(),this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(episodeRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        episodeRecycleViewAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        episodeViewModel.getLiveCharactersMutableLiveData().removeObservers(this);
    }
}
