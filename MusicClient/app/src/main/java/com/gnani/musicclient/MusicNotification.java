package com.gnani.musicclient;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class MusicNotification extends Service {

    private static final String TAG = "MusicNotification";
    private static final int NOTIFICATION_ID = 1;
    private static String URL = "";
    private static String CHANNEL_ID = "Music player";
    private MediaPlayer mediaPlayer;
    private int mStartID;
    private Notification notification;


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
        createNotificationChannel();
        Intent notificationIntent = new Intent(getApplicationContext(), MusicActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        notification =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setOngoing(true).setContentTitle("Music Playing")
                        .setContentText("Click to Access Music Player")
                        .setTicker("Music is playing!")
                        .setFullScreenIntent(pendingIntent, false)
                        .build();

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        Log.i(TAG, "createNotificationChannel: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music player notification";
            String description = "The channel for music player notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        URL = intent.getStringExtra("URL");

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(URL);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            mStartID = startId;
            if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(URL);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
