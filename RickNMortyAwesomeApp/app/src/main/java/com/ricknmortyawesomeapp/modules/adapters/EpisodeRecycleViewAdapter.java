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
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.viewmodels.EpisodeViewModel;
import com.ricknmortyawesomeapp.modules.views.CharacterActivity;
import com.ricknmortyawesomeapp.modules.views.EpisodeActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EpisodeRecycleViewAdapter extends RecyclerView.Adapter<EpisodeRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Result> dead;
    private List<Result> alive;
    private Context mContext;

    public EpisodeRecycleViewAdapter(List<Result> alive, List<Result> dead, Context mContext) {
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

        for (int i = 0; i < 2; i++) {
            final List<Result> characters = (i==0?alive:dead);
            final CircleImageView imageView = (i==0?holder.alive:holder.dead);

            if (holderPosition<characters.size()) {
                episodeActivity.episodeViewModel.getImage(characters.get(holderPosition).image, new EpisodeViewModel.ImageDownloadListener() {
                    @Override
                    public void onEvent(Bitmap bitmap, Exception error) {
                        if (error == null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Result resultCharacters = characters.get(holderPosition);
                                    Log.d(TAG, "onClick: clicked on: " + resultCharacters.name);

                                    Toast.makeText(mContext, resultCharacters.name, Toast.LENGTH_SHORT).show();
                                    episodeActivity.episodeViewModel.setSelectedCharacter(resultCharacters.id);
                                    Intent intent = new Intent(mContext, CharacterActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            imageView.setImageResource(0);
                            imageView.setOnClickListener(null);
                        }
                    }
                });
            } else {
                imageView.setImageResource(0);
                imageView.setOnClickListener(null);
            }
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
