package com.example.amgad.imovies;

import android.content.Context;
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

public class reviewAdapter extends ArrayAdapter<reviewObject> {

    ArrayList<reviewObject> objects;

    private static class ViewHolder {
        private TextView Author, content;
    }

    public reviewAdapter(Context context, int resource, int textViewResourceId, ArrayList<reviewObject> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.review_item, parent, false);

            viewHolder.Author = (TextView) convertView.findViewById(R.id.review_author);
            viewHolder.content = (TextView) convertView.findViewById(R.id.review_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        reviewObject item = getItem(position);
        if (item != null) {

            viewHolder.Author.setText(item.getAuthor());
            viewHolder.content.setText(item.getContent());
        }
        return convertView;
    }
}
