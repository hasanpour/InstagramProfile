package com.example.instagramprofile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.instagramprofile.R;
import com.example.instagramprofile.model.Media;

/**
 * An adapter to inflate Grid View
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private List<Media> mediaList;
    private Context context;

    public GridAdapter(Context context, List<Media> mediaList) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_layout_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Media image = mediaList.get(position);
        Picasso.with(context).load(image.getImageURL()).into(holder.imageViewImage); // Loading image using Picasso library
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewImage;

        public MyViewHolder(View view) {
            super(view);
            imageViewImage = (ImageView) view.findViewById(R.id.imageViewImage);
        }
    }
}
