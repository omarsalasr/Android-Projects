package com.osalas3project5.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.osalas3project5.common.ClipInterface;

public class ClipService extends Service {

    // Channel ID for the notification manager
    private final String CHANNEL_ID = "Clip player channel";
    // MediaPlayer instance for clip playback
    private MediaPlayer clipPlayer;
    // Current clip
    private int currClip;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create channel SDK >= Oreo
        createNotificationChannel();
        // Create notification intent and pending intent
        Intent notificationIntent = new Intent(getApplicationContext(), getApplicationContext().getClass());
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        // Build notification
        Notification notification =
                new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setOngoing(true)
                        .setContentTitle("Clip Playing")
                        .setContentText("Click to Access Clip Player")
                        .setTicker("Clip is playing")
                        .setFullScreenIntent(pendingIntent, false)
                        .build();

        int NOTIFICATION_ID = 1;
        // Start service with ongoing foreground notification so it stays alive
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ACTION_KEY = "SERVICE_ACTION";
        int ACTION_STOP = 2;
        // Check if the service needs to stop itself instead of starting
        if(intent.getIntExtra(ACTION_KEY,1) == ACTION_STOP){
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(clipPlayer != null){
            // Stop clip playback
            clipPlayer.stop();
        }
    }

    // AIDL Stub interface for handling remote calls
    ClipInterface.Stub binder = new ClipInterface.Stub() {

        @Override
        public void playClip(int clipNumber){
            currClip = clipNumber;
            // Initiate MediaPlayer object with the current clip selected
            clipPlayer = MediaPlayer.create(getApplicationContext(), getClip());
            if(clipPlayer != null){ // MediaPlayer created successfully
                // Set clip player parameters and start playback
                clipPlayer.setLooping(false);
                clipPlayer.start();
                clipPlayer.seekTo(0);
                clipPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopClip(); // Stop the clip when done
                    }
                });
            }
        }

        @Override
        public void resumeClip(){
            if(clipPlayer != null){
                clipPlayer.start(); // Resume clip playback
            }
        }

        @Override
        public void pauseClip(){
            if(clipPlayer != null){
                clipPlayer.pause(); // Pause clip playback
            }
        }

        @Override
        public void stopClip(){
            if(clipPlayer != null){
                clipPlayer.stop(); // Stop clip playback
            }
        }

        @Override
        public void stopService(){
            stopSelf();
        }
    };

    private void createNotificationChannel(){
        // Create a notification channel for device running SDK >= Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Clip Player notification";
            String desc = "The channel for clip player notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(desc);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Get the clip from raw for currentclip selected
    private int getClip(){
        switch(currClip){
            case(1): return R.raw.clip1;
            case(2): return R.raw.clip2;
            case(3): return R.raw.clip3;
            case(4): return R.raw.clip4;
            case(5): return R.raw.clip5;
        }
        return 1;
    }
}