package com.example.amgad.imovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.amgad.imovies.MainFragment.EXTRA_MOVIE_ID;

/**
 * Created by Amgad on 09-Oct-16.
 */

class movieAdapter extends RecyclerView.Adapter<movieAdapter.movieAdapterViewHolder> {
    private List<movieObject> movieObjectArrayList;

    private LayoutInflater mInflater;
    private Context mContext;

    movieAdapter(Context context, List<movieObject> results) {
        movieObjectArrayList = results;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public class movieAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;

        public movieAdapterViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.imageViewItem);
        }
    }

    public int getCount() {
        return movieObjectArrayList.size();
    }

    public Object getItem(int position) {
        return movieObjectArrayList.get(position);
    }

    @Override
    public movieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new movieAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(movieAdapterViewHolder holder, final int position) {
        movieObject movie = movieObjectArrayList.get(position);

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + movie.getImage()).into(holder.imageview);

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetail.class);
                movieObject movieObject = movieObjectArrayList.get(position);
                intent.putExtra(EXTRA_MOVIE_ID, movieObject.getId());
                mContext.startActivity(intent);
            }
        });
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movieObjectArrayList.size();
    }

}
