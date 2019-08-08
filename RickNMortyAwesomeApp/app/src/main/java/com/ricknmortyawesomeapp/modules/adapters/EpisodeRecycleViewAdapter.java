package com.ricknmortyawesomeapp.modules.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EpisodeRecycleViewAdapter extends RecyclerView.Adapter<EpisodeRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Result> episodes;
    private List<ResultCharacters> characters;
    private List<ResultCharacters> dead;
    private List<ResultCharacters> alive;
    private Context mContext;
    private int selectedIndex;

    public EpisodeRecycleViewAdapter(List<Result> episodes, List<ResultCharacters> characters, int selected, Context mContext) {
        this.episodes = episodes;
        this.characters = characters;
        this.mContext = mContext;
        selectedIndex = selected;
        dead = new ArrayList<>();
        alive = new ArrayList<>();

        setupData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_episodeitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if (position<alive.size()) {
            Picasso.with(mContext).load(alive.get(position).image).into(holder.alive);
        }
        if (position<dead.size()) {
            Picasso.with(mContext).load(dead.get(position).image).into(holder.dead);
        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + titles.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return characters.size()>0? dead.size()>alive.size()? dead.size():alive.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView alive, dead;
        LinearLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            alive = itemView.findViewById(R.id.alive);
            dead = itemView.findViewById(R.id.dead);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void setupData() {
        if (alive.size() == 0 && dead.size()==0) {
            List<Integer> charIDs = new ArrayList<>();
            for (String result : episodes.get(selectedIndex).characters) {
                charIDs.add(Integer.valueOf(result.replace("https://rickandmortyapi.com/api/character/", "")));
            }
            if (characters.size() > charIDs.get(charIDs.size() - 1)) {
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
    }
}
