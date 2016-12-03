package com.example.amgad.imovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Amgad on 03-Dec-16.
 */

public class trailerAdapter extends RecyclerView.Adapter<trailerAdapter.trailerAdapterViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;

    trailerAdapter(Context context, List<movieObject> results) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
public class trailerAdapterViewHolder extends RecyclerView.ViewHolder{

    ListView listView;

    public trailerAdapterViewHolder(View itemView) {
        super(itemView);
        listView = (ListView) itemView.findViewById(R.id.trailer);
    }
}

    @Override
    public trailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.trailer_item, parent, false);

        return new trailerAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(trailerAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
