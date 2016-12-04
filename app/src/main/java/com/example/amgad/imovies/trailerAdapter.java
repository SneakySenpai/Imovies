package com.example.amgad.imovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amgad on 03-Dec-16.
 */

public class trailerAdapter extends ArrayAdapter<trailerObject> {
    ArrayList<trailerObject> objects;
    Context context;

    private static class ViewHolder {
        private TextView itemView;
    }

    public trailerAdapter(Context context, int resource, int textViewResourceId, ArrayList<trailerObject> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.trailer_item, parent, false);

            viewHolder.itemView = (TextView) convertView.findViewById(R.id.trailer_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final trailerObject item = getItem(position);
        if (item != null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item.getTitle());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.getKey()));
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
