package com.ricknmortyawesomeapp.modules.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.DataModel;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.viewmodels.EpisodeViewModel;
import com.ricknmortyawesomeapp.modules.viewmodels.MainViewModel;
import com.ricknmortyawesomeapp.modules.adapters.MainRecycleViewAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public MainViewModel mainViewModel;
    private EpisodeViewModel episodeViewModel;
    private MainRecycleViewAdapter mainRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("RickNMorty Episode Guide");

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getEpisodesNamesMutableLiveData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> dataModel) {
                mainRecycleViewAdapter.notifyDataSetChanged();
            }
        });

        mainRecycleViewAdapter = new MainRecycleViewAdapter(this, mainViewModel.getEpisodesNamesMutableLiveData().getValue());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(mainRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainViewModel.getEpisodesNamesMutableLiveData().removeObservers(this);
    }
}
