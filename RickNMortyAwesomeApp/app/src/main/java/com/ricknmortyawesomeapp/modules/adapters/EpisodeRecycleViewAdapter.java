package com.ricknmortyawesomeapp.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.views.CharacterActivity;
import com.ricknmortyawesomeapp.modules.views.EpisodeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EpisodeRecycleViewAdapter extends RecyclerView.Adapter<EpisodeRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<ResultCharacters> dead;
    private List<ResultCharacters> alive;
    private Context mContext;

    public EpisodeRecycleViewAdapter(List<ResultCharacters> alive, List<ResultCharacters> dead, Context mContext) {
        this.mContext = mContext;
        this.dead = dead;
        this.alive = alive;
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
        } else {
            holder.alive.setImageResource(0);
        }
        if (position<dead.size()) {
            Picasso.with(mContext).load(dead.get(position).image).into(holder.dead);
        } else {
            holder.dead.setImageResource(0);
        }

        holder.alive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResultCharacters resultCharacters = alive.get(position);
                Log.d(TAG, "onClick: clicked on: " + resultCharacters.name);

                Toast.makeText(mContext, resultCharacters.name, Toast.LENGTH_SHORT).show();
                ((EpisodeActivity)mContext).episodeViewModel.setSelectedCharacter(resultCharacters.id);
                Intent intent = new Intent(mContext, CharacterActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.dead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResultCharacters resultCharacters = dead.get(position);
                Log.d(TAG, "onClick: clicked on: " + resultCharacters.name);

                Toast.makeText(mContext, resultCharacters.name, Toast.LENGTH_SHORT).show();
                ((EpisodeActivity)mContext).episodeViewModel.setSelectedCharacter(resultCharacters.id);
                Intent intent = new Intent(mContext, CharacterActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dead.size()>alive.size()? dead.size():alive.size();
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
}
