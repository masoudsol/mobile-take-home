package com.ricknmortyawesomeapp.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricknmortyawesomeapp.R;
import com.ricknmortyawesomeapp.modules.models.DataModel;
import com.ricknmortyawesomeapp.modules.models.Result;
import com.ricknmortyawesomeapp.modules.views.EpisodeActivity;
import com.ricknmortyawesomeapp.modules.views.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Result> titles;
//    private List<String> espisodes;
//    private List<String> airdates;
    private Context mContext;

    public MainRecycleViewAdapter(Context context, List<Result> titles) {
        this.titles = titles;
//        mImages = images;
        mContext = context;
//        this.espisodes = episodes;
//        this.airdates = airdates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.title);

        final Result result = titles.get(position);
        holder.episode.setText(result.episode);
        holder.title.setText(result.name);
        SimpleDateFormat fmt;
        fmt = new SimpleDateFormat("MMMMM dd, yyyy", Locale.CANADA);
        Date date = null;
        try {
            date = fmt.parse(result.air_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d, yy", Locale.CANADA);
        holder.airDate.setText(fmtOut.format(date));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + result.name);

                Toast.makeText(mContext, result.name, Toast.LENGTH_SHORT).show();
                ((MainActivity)mContext).mainViewModel.setEpisodeSelected(position);

                Intent intent = new Intent(mContext, EpisodeActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (titles != null){
            return titles.size();
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, episode,airDate;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            episode = itemView.findViewById(R.id.episode);
            airDate = itemView.findViewById(R.id.airdate);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
