package com.ricknmortyawesomeapp.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.ResultCharacters;
import com.ricknmortyawesomeapp.modules.viewmodels.EpisodeViewModel;
import com.ricknmortyawesomeapp.modules.views.CharacterActivity;
import com.ricknmortyawesomeapp.modules.views.EpisodeActivity;

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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_episodeitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.position = viewType;
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        final EpisodeActivity episodeActivity = (EpisodeActivity)mContext;

        final int holderPosition = holder.position;

        if (holderPosition<alive.size()) {
            episodeActivity.episodeViewModel.getImage(alive.get(holderPosition).image, new EpisodeViewModel.ImageDownloadListener() {
                @Override
                public void onEvent(Bitmap bitmap, Exception error) {
                    if (error == null && bitmap != null) {
                        holder.alive.setImageBitmap(bitmap);
                        holder.alive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ResultCharacters resultCharacters = alive.get(holderPosition);
                                Log.d(TAG, "onClick: clicked on: " + resultCharacters.name);

                                Toast.makeText(mContext, resultCharacters.name, Toast.LENGTH_SHORT).show();
                                episodeActivity.episodeViewModel.setSelectedCharacter(resultCharacters.id);
                                Intent intent = new Intent(mContext, CharacterActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        holder.alive.setImageResource(0);
                        holder.alive.setOnClickListener(null);
                    }
                }
            });
        } else {
            holder.alive.setImageResource(0);
            holder.alive.setOnClickListener(null);
        }

        if (holderPosition<dead.size()) {
            episodeActivity.episodeViewModel.getImage(dead.get(holderPosition).image, new EpisodeViewModel.ImageDownloadListener() {
                @Override
                public void onEvent(Bitmap bitmap, Exception error) {
                    if (error == null && bitmap != null) {
                        Log.d(TAG, "onBindViewHolder: "+dead.get(holderPosition).name+ " Added "+holderPosition);
                        holder.dead.setImageBitmap(bitmap);
                        holder.dead.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ResultCharacters resultCharacters = dead.get(holderPosition);
                                Log.d(TAG, "onClick: clicked on: " + resultCharacters.name);

                                Toast.makeText(mContext, resultCharacters.name, Toast.LENGTH_SHORT).show();
                                ((EpisodeActivity)mContext).episodeViewModel.setSelectedCharacter(resultCharacters.id);
                                Intent intent = new Intent(mContext, CharacterActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        Log.d(TAG, "onBindViewHolder: "+dead.get(holderPosition).name+ "removed "+holderPosition);
                        holder.dead.setImageResource(0);
                        holder.dead.setOnClickListener(null);
                    }
                }
            });
        } else  {
            Log.d(TAG, "onBindViewHolder: removed "+holderPosition);
            holder.dead.setImageResource(0);
            holder.dead.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return dead.size()>alive.size()? dead.size():alive.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView alive, dead;
        LinearLayout parentLayout;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            alive = itemView.findViewById(R.id.alive);
            dead = itemView.findViewById(R.id.dead);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
