package com.gnani.musicclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gnani.music.MusicInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MusicInterface musicInterface;
    private boolean isBound;
    private Button bindButton;
    private Button unbindButton;
    private Button musicButton;
    private static List<MusicModel> songs;
    RecyclerView rView;
    MusicAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView mImageView;
    private MediaPlayer mediaPlayer;
    private int id;

    private String[] title = new String[6];
    private String[] artist = new String[6];
    private Bitmap[] image = new Bitmap[6];
    private String[] url = new String[6];
    private byte[][] byteArray = new byte[6][];

    //Service Connection to get the values from Music Central
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicInterface = MusicInterface.Stub.asInterface(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicInterface = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindButton = (Button) findViewById(R.id.bind);
        unbindButton = (Button) findViewById(R.id.unbind);
        musicButton = (Button) findViewById(R.id.songs);


        // OnclickListener for buttons .
        bindButton.setOnClickListener(v -> {
            onBind();
        });

        unbindButton.setOnClickListener(v -> {
            onUnbind();
        });

        musicButton.setOnClickListener(v -> {
            onUnbind();
            try {
                List<List<Object>> songs;
                Bundle bundle = new Bundle();
                Log.i("TAG", "onCreate:2 " + musicInterface.getSongs());
                songs = musicInterface.getSongs();

                for(int i=0; i < 6 ; i++) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    title[i] = (String) songs.get(i).get(0);
                    artist[i] = (String) songs.get(i).get(1);
                    url[i] = (String) songs.get(i).get(2);
                    image[i] = (Bitmap) songs.get(i).get(3);
                    String imageString = "image" + i;
                    image[i].compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray[i] = stream.toByteArray();
                    bundle.putByteArray(imageString, byteArray[i]);
                }

                bundle.putStringArray("title", title);
                bundle.putStringArray("artist", artist);
                bundle.putStringArray("url", url);

                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });
        isBound = false;
        bindButton.setEnabled(true);
        unbindButton.setEnabled(false);
        musicButton.setEnabled(false);
    }

    //Method to unbind .
    private void onUnbind() {

        if (isBound) {
            unbindService(this.mConnection);
            isBound = false;
            bindButton.setEnabled(true);
            unbindButton.setEnabled(false);
            musicButton.setEnabled(false);
        }
    }

    //Method to bind the values .
    private void onBind() {

        if (!isBound) {
            Intent intent = new Intent(MusicInterface.class.getName());
            Log.i("bind", "onBind: " + MusicInterface.class.getName());
            ResolveInfo info = getPackageManager().resolveService(intent, 0);
            Log.i("bind", "onBind: " + info);
            intent.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
            Boolean bool = bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);
            bindButton.setEnabled(false);
            unbindButton.setEnabled(true);
            musicButton.setEnabled(true);
        }
    }


}