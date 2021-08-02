package com.gnani.musiccentral;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gnani.music.MusicInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyService extends Service {
    private String[] title;
    private String[] artist;
    private String[] url;
    private static List songs = new ArrayList();
    private final static Bundle bundle = new Bundle();
    private ArrayList<Integer> image = new ArrayList<>(
            Arrays.asList(R.drawable.amber, R.drawable.arch, R.drawable.coffee, R.drawable.drupal, R.drawable.itune,
                    R.drawable.keybase));

    private final MusicInterface.Stub mBinder = new MusicInterface.Stub() {

        @Override
        public List getSongs() throws RemoteException {
            songs.clear();

            title = getResources().getStringArray(R.array.title);
            artist = getResources().getStringArray(R.array.artist);
            url = getResources().getStringArray(R.array.url);

            for (int i = 0; i < image.size(); i++) {
                songs.add(Arrays.asList(title[i], artist[i], url[i], ((BitmapDrawable) getDrawable(image.get(i))).getBitmap()));
            }
//            bundle.putParcelableArrayList("songs", (ArrayList<? extends Parcelable>) songs);
            Log.i("TAG", "getSongs: " + bundle) ;
            return songs;
        }

    };

//    @Override
//    public Object getSongDetails(int id) throws RemoteException {
//        return songs.get(id);
//    }
//
//
//    @Override
//    public String getURL(int id) throws RemoteException {
//        return url[id];
//    }

    //Ibinder to send the stub to application
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}