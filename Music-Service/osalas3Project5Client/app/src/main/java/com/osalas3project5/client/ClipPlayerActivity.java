package com.osalas3project5.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.osalas3project5.common.ClipInterface;

public class ClipPlayerActivity extends AppCompatActivity {

    // AIDL object that is used for IPC with remote service
    private ClipInterface clipService;
    // Service Actions, stop service or start service
    private final int ACTION_START = 1;
    private final int ACTION_STOP = 2;
    // Service package name and class for explicit intent
    private final String PKG = "com.osalas3project5.service";
    private final String CLASS = "com.osalas3project5.service.ClipService";
    // Check the state of the clip player application for UI updating
    private boolean isBound;
    private boolean isStarted;
    private boolean isPaused;
    // Button views
    private Button btnStartService, btnStopService, btnPlayStop, btnPauseResume;
    // Current clip number selected and boolean to check if any has been selected
    private int currClip = -1;
    private boolean clipSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // List View to display the clips
        ListView clipListView = findViewById(R.id.list_view_clips);
        String[] clips = {"Bad News", "Zombies", "Goku Scream", "Naruto Rasengan", "Lightsaber"};
        clipListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,clips));
        clipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clipSelected = true;
                if(currClip != position+1){ // Check if a new clip has been selected
                    currClip = position + 1;
                    if(isBound) { // If clip is playing, stop the clip
                        stopClip();
                        unbindService();
                    }
                }
            }
        });

        btnStartService = findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarted) // Start the service if it hasn't been started
                    serviceAction(ACTION_START);
                updateUI();
            }
        });

        btnStopService = findViewById(R.id.btn_stop_service);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStarted) // Stop the service if it has been started
                    serviceAction(ACTION_STOP);
                updateUI();
            }
        });

        btnPlayStop = findViewById(R.id.btn_play_stop_clip);
        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clipSelected){ // Check if a clip was ever selected
                    Toast.makeText(getApplicationContext(), "Please choose a clip", Toast.LENGTH_SHORT).show();
                }else{ // Clip selected
                    if(isBound){ // Clip is currently playing, stop the playback
                        stopClip();
                        unbindService();
                    }else // Clip isn't playing, start the playback
                        bindService();
                    updateUI();
                }
            }
        });

        btnPauseResume = findViewById(R.id.btn_pause_resume_clip);
        btnPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused){ // Check is paused, resume the clip
                    isPaused = false;
                    resumeClip();
                }else{ // Currently playing, pause the clip
                    isPaused = true;
                    pauseClip();
                }
                updateUI();
            }
        });
        // Update the initial UI
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the service when activity is destroyed
        serviceAction(ACTION_STOP);
    }

    // Play the current clip selected
    private void playClip() {
        try{
            // Call AIDL playclip on remote service
            clipService.playClip(currClip);
        }catch (Exception e){
            Log.e("Client", e.toString());
        }
    }

    // Stop the current clip that is playing
    private void stopClip() {
        try{
            // Call AIDL stopclip on remote service
            clipService.stopClip();
        }catch (Exception e){
            Log.e("Client", e.toString());
        }
    }

    // Resume the current clip when paused
    private void resumeClip() {
        try{
            // Call AIDL resumeclip on remote service
            clipService.resumeClip();
        }catch (Exception e){
            Log.e("Client", e.toString());
        }
    }

    // Pause the current clip when it's currently playing
    private void pauseClip() {
        try{
            // Call AIDL pauseclip on remote service
            clipService.pauseClip();
        }catch (Exception e){
            Log.e("Client", e.toString());
        }
    }

    // Helper function that starts or stops the service
    private void serviceAction(int action){
        // Service intent
        Intent i = new Intent().setClassName(PKG, CLASS);
        String ACTION_KEY = "SERVICE_ACTION";
        // Put the service desired action in extras (start or stop)
        i.putExtra(ACTION_KEY, action);
        if(action == ACTION_START){ // Start the service
            startForegroundService(i);
            isStarted = true;
        }else{ // Stop the service
            if(isBound)
                unbindService();
            startForegroundService(i);
            isStarted = false;
            isBound = false;
            isPaused = false;
        }
    }

    // Helper function that binds the service with explicit intent and current connection
    private void bindService(){
        Intent i = new Intent().setClassName(PKG, CLASS);
        bindService(i, conn,BIND_AUTO_CREATE);
    }

    // Current connection anonymous class for bound service starting
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Get the IBinder object for IPC communication
            clipService = ClipInterface.Stub.asInterface(service);
            isBound = true;
            updateUI();
            playClip(); // Start playing the clip
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            clipService = null;
        }
    };

    // Helper function that unbinds the service with current connection
    private void unbindService(){
        isBound = false;
        updateUI();
        unbindService(conn);
        serviceAction(ACTION_START);// Continue the started service
    }

    // Helper function that updates the UI according to the current state of the clip playback
    private void updateUI(){
        if(isStarted){ // Service started
            if(isBound){ // Clip currently playing/selected to play
                if(isPaused){ // Clip is paused
                    btnStartService.setEnabled(false);
                    btnStopService.setEnabled(true);
                    btnPlayStop.setEnabled(true);
                    btnPlayStop.setText(R.string.stop);
                    btnPauseResume.setEnabled(true);
                    btnPauseResume.setText(R.string.resume);
                }else{ // Clip not paused
                    btnStartService.setEnabled(false);
                    btnStopService.setEnabled(true);
                    btnPlayStop.setEnabled(true);
                    btnPlayStop.setText(R.string.stop);
                    btnPauseResume.setEnabled(true);
                    btnPauseResume.setText(R.string.pause);
                }
            }else{ // Clip not playing
                btnStartService.setEnabled(false);
                btnStopService.setEnabled(true);
                btnPlayStop.setEnabled(true);
                btnPlayStop.setText(R.string.play);
                btnPauseResume.setEnabled(false);
                btnPauseResume.setText(R.string.pause);
            }
        }else{ // Service not started
            btnStartService.setEnabled(true);
            btnStopService.setEnabled(false);
            btnPlayStop.setEnabled(false);
            btnPlayStop.setText(R.string.play);
            btnPauseResume.setEnabled(false);
            btnPauseResume.setText(R.string.pause);
        }
    }
}
