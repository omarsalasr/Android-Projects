package com.example.tankcontroller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ControllerActivity extends AppCompatActivity {

    private int tankSpeed;
    private String address;
    private ProgressDialog progress;
    private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Intent i = getIntent();
        address = i.getStringExtra(PairingActivity.EXTRA_ADDRESS);
        new ConnectBT().execute();

        Button btnDropSalt = findViewById(R.id.drop_salt);
        Button btnForward = findViewById(R.id.btnForward);
        Button btnReverse = findViewById(R.id.btnReverse);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnDisc = findViewById(R.id.btnDisc);
        SeekBar tank_speed_bar = findViewById(R.id.tank_speed_bar);
        tankSpeed = 0;

        btnDropSalt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action =  event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                    dropSalt();
                else if(action == MotionEvent.ACTION_UP)
                    moveStop();
                return false;
            }
        });

        btnForward.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action =  event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                    moveForward();
                else if(action == MotionEvent.ACTION_UP)
                    moveStop();
                return false;
            }
        });

        btnReverse.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action =  event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                    moveReverse();
                else if(action == MotionEvent.ACTION_UP)
                    moveStop();
                return false;
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action =  event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                    moveLeft();
                else if(action == MotionEvent.ACTION_UP)
                    moveStop();
                return false;
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action =  event.getAction();
                if(action == MotionEvent.ACTION_DOWN)
                    moveRight();
                else if(action == MotionEvent.ACTION_UP)
                    moveStop();
                return false;
            }
        });


        btnDisc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Disconnect(); //close connection
            }
        });

        tank_speed_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tankSpeed = progress + 180;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateSpeed();
            }
        });

    }

    private void dropSalt(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("DROP".getBytes());
            }catch (IOException e){
                Log.e("DROP: ", e.toString());
            }
        }
    }

    private void updateSpeed(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(("SPEED" + tankSpeed).getBytes());
            }catch(IOException e){
                Log.e("TANK SPEED: ", e.toString());
            }
        }
    }

    private void Disconnect(){
        if (btSocket!=null){ //If the btSocket is busy
            try{
                btSocket.close(); //close connection
            }catch (IOException e){
                Log.e("DISCONNECT: ", e.toString());
            }
        }
        finish(); //return to the first layout

    }

    private void moveForward(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("FORWARD".getBytes());
            }catch (IOException e){
                Log.e("FORWARD: ", e.toString());
            }
        }
    }

    private void moveReverse(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("REVERSE".getBytes());
            }catch (IOException e){
                Log.e("REVERSE: ", e.toString());
            }
        }
    }

    private void moveLeft(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("LEFT".getBytes());
            }catch (IOException e){
                Log.e("LEFT: ", e.toString());
            }
        }
    }

    private void moveRight(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("RIGHT".getBytes());
            }catch (IOException e){
                Log.e("RIGHT: ", e.toString());
            }
        }
    }

    private void moveStop(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("STOP".getBytes());
            }catch (IOException e){
                Log.e("STOP: ", e.toString());
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> { //Bluetooth Thread
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(ControllerActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices){ //while the progress dialog is shown, the connection is done in background
            try {
                if (btSocket == null || !isBtConnected){
                    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dev = btAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dev.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e){
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){ //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(),"Connection Failed. Is it a SPP Bluetooth? Try again.",Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Connected.",Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
