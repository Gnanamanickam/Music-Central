package com.gnani.musicclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MusicModel> musicArray;
    private String[] title;
    private String[] artist;
    private Bitmap[] image = new Bitmap[6];
    private ClickListener listener;

    // Constructor to assign the values .
    public MusicAdapter(String[] title, String[] artist,
                        Bitmap[] image, ClickListener listener) {
        this.title = title;
        this.artist = artist;
        this.listener = listener;
        this.image = image;
    }

    // Create the view
    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);

        return viewHolder;
    }

    // To set the values .
    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {

        holder.mTitle.setText(title[position]);
        holder.mArtist.setText(artist[position]);
        holder.mImage.setImageBitmap(image[position]);

    }

    // Class creates a wrapper object around a view that contains the layout for  each individual item .
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTitle;
        public TextView mArtist;
        public ImageView mImage;
        private ClickListener listener;

        public ViewHolder(@NonNull View itemView, ClickListener clistener) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.title);
            mArtist = itemView.findViewById(R.id.artist);
            mImage = itemView.findViewById(R.id.imageView);

            this.listener = clistener;

            itemView.setOnClickListener(this);
        }

        // Functionality to implement on clickable
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }

    }

    // Return item count .
    @Override
    public int getItemCount() {
        return title.length;
    }
}
