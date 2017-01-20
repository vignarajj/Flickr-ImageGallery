package com.flickr.imgallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    Context context;
    List<FlickrModel> feedList;
    private LayoutInflater inflator = null;
    private ViewHolder vholder;

    public GridAdapter(Context context, List<FlickrModel> feedList) {
        super();
        this.context = context;
        this.feedList = feedList;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.row_feed, null);
            vholder = new ViewHolder(convertView);
            vholder.row_image = (ImageView) convertView.findViewById(R.id.row_image);
            convertView.setTag(vholder);
        } else {
            vholder = (ViewHolder) convertView.getTag();
        }
        if (feedList.get(position).getmPictureUrl() != null) {
            Picasso.with(context).load(feedList.get(position).getmPictureUrl()).placeholder(R.drawable.loading_image).noFade().resize(150, 150).into(vholder.row_image);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView row_image;
        ViewHolder(View view) {
            this.row_image = (ImageView) view.findViewById(R.id.row_image);
        }
    }
}
