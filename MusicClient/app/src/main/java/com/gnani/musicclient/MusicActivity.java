package com.gnani.musicclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicActivity extends AppCompatActivity  {

    private static final String TAG = "MusicActivity";
    private static List<MusicModel> songs;
    RecyclerView rView;
    RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView mImageView;
    private int id;
    private String[] title = new String[6];
    private String[] artist = new String[6];
    private Bitmap[] image = new Bitmap[6];
    private String[] url = new String[6];


    private Bundle bundle;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);

        bundle = getIntent().getExtras();
        title = bundle.getStringArray("title");
        artist = bundle.getStringArray("artist");
        url = bundle.getStringArray("url");

        byte[][] byteArray = new byte[7][];
        String imageString;
        for (int i = 0; i < 6; i++) {
            imageString = "image" + i;
            byteArray[i] = getIntent().getByteArrayExtra(imageString);
            image[i] = BitmapFactory.decodeByteArray(byteArray[i], 0, byteArray[i].length);
        }

        //Listener to access the information
        ClickListener listener = (view,position)->{
            startMusic(url[position]);
            Log.i(TAG, "onCreate: " + url[position]);
            Intent intent = new Intent(this,
                    MusicNotification.class);
            intent.putExtra("URL", url[position]);
            startService(intent);
        };

        rView = findViewById(R.id.recyclerView);
        rView.setLayoutManager(new LinearLayoutManager(this)); // For view
        rView.setHasFixedSize(true);
        mAdapter = new MusicAdapter(title, artist, image, listener);
        rView.setAdapter(mAdapter);
    }

    //Method to start music on click .
    private void startMusic(String url) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void stopMusic(){
        Intent intent = new Intent(getApplicationContext(), MusicNotification.class);
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bindFunction();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        stopMusic();
        Log.i(TAG, "onDestroy:");
    }

}
