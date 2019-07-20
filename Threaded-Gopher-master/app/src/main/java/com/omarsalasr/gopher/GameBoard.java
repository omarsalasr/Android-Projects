package com.omarsalasr.gopher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard extends AppCompatActivity {

    private int currGameMode; // MbM -> 0, Cont -> 1
    private int currPlayer;
    private int[] gopherLocation; // Gopher row and col coordinate
    private boolean gameOver = false;
    private TextView currPlayerTxt, currGameModeTxt;
    private Button MbMButton;
    private int[][] gameBoardInformation = new int[10][10]; // Information grid containing game grid
    private int[][] gameBoardViews = { // View ID grid containing the visual representation of the info grid
            {R.id.R0C0,R.id.R0C1,R.id.R0C2,R.id.R0C3,R.id.R0C4,R.id.R0C5,R.id.R0C6,R.id.R0C7,R.id.R0C8,R.id.R0C9},
            {R.id.R1C0,R.id.R1C1,R.id.R1C2,R.id.R1C3,R.id.R1C4,R.id.R1C5,R.id.R1C6,R.id.R1C7,R.id.R1C8,R.id.R1C9},
            {R.id.R2C0,R.id.R2C1,R.id.R2C2,R.id.R2C3,R.id.R2C4,R.id.R2C5,R.id.R2C6,R.id.R2C7,R.id.R2C8,R.id.R2C9},
            {R.id.R3C0,R.id.R3C1,R.id.R3C2,R.id.R3C3,R.id.R3C4,R.id.R3C5,R.id.R3C6,R.id.R3C7,R.id.R3C8,R.id.R3C9},
            {R.id.R4C0,R.id.R4C1,R.id.R4C2,R.id.R4C3,R.id.R4C4,R.id.R4C5,R.id.R4C6,R.id.R4C7,R.id.R4C8,R.id.R4C9},
            {R.id.R5C0,R.id.R5C1,R.id.R5C2,R.id.R5C3,R.id.R5C4,R.id.R5C5,R.id.R5C6,R.id.R5C7,R.id.R5C8,R.id.R5C9},
            {R.id.R6C0,R.id.R6C1,R.id.R6C2,R.id.R6C3,R.id.R6C4,R.id.R6C5,R.id.R6C6,R.id.R6C7,R.id.R6C8,R.id.R6C9},
            {R.id.R7C0,R.id.R7C1,R.id.R7C2,R.id.R7C3,R.id.R7C4,R.id.R7C5,R.id.R7C6,R.id.R7C7,R.id.R7C8,R.id.R7C9},
            {R.id.R8C0,R.id.R8C1,R.id.R8C2,R.id.R8C3,R.id.R8C4,R.id.R8C5,R.id.R8C6,R.id.R8C7,R.id.R8C8,R.id.R8C9},
            {R.id.R9C0,R.id.R9C1,R.id.R9C2,R.id.R9C3,R.id.R9C4,R.id.R9C5,R.id.R9C6,R.id.R9C7,R.id.R9C8,R.id.R9C9},
    };

    // Constant values for the type of move
    private final int GOPHER = 4;
    private final int MISS = 3;
    private final int CLOSE = 2;
    private final int NEAR = 1;
    private final int EMPTY = 0;
    // Constant values for handler messages
    private final int MAKE_MOVE = 10;
    private final int PROCESS_MOVE = 9;
    // Constant values for the different game modes
    public final static int MODE_CONT = 12;
    public final static int MODE_MbM = 13;
    // Constant values for the 2 players
    private final int PLAYER_1 = 16;
    private final int PLAYER_2 = 17;
    // Key value for intent extras
    public final static String WINNER = "GAME_WINNER";
    private final int CONFIRMATION_RESULT = 20;

    // Worker threads for the game with different algorithms
    private SmartThread smartThread = new SmartThread("Smart Thread");
    private RandomThread randomThread = new RandomThread("Random Thread");
    // Handler for the UI
    private Handler UIHandler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // Set the board data to 0s
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                gameBoardInformation[i][j] = EMPTY;

        // Pick random gopher location and update the info grid
        gopherLocation = new int[]{(int) (Math.random() * 10),(int) (Math.random() * 10)};
        gameBoardInformation[gopherLocation[0]][gopherLocation[1]] = GOPHER;

        // Get the game information from the intent
        Intent i = getIntent();
        currGameMode = i.getIntExtra(MainMenu.GAME_MODE,MODE_MbM);
        currPlayer= (int) (Math.random() * 2); // Pick random player

        // Start the threads if not started
        if(smartThread.getState() == Thread.State.NEW)
            smartThread.start();
        if(randomThread.getState() == Thread.State.NEW)
            randomThread.start();

        // Get UI elements
        currPlayerTxt = findViewById(R.id.txt_curr_player);
        currGameModeTxt = findViewById(R.id.txt_curr_mode);
        MbMButton = findViewById(R.id.btn_Move);
        // When in Move-by-Move, send handler message when button clicked
        MbMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { sendMoveMessage(); }
        });

        Button changeModeButton = findViewById(R.id.btn_Mode);
        changeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
                updateScreenInformation();
                // If the mode changed to continuous, send handler a message
                if(currGameMode == MODE_CONT)
                    sendMoveMessage();
            }
        });

        // UI Handler that receives messages to process the move given by a worker thread
        UIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what){
                    case PROCESS_MOVE: // Process the given move
//                        printGrid(); // Debugging
                        changePlayer();
                        updateScreenInformation();
                        // If in continuous move, continuously send message to the next worker thread
                        if(currGameMode == MODE_CONT)
                            sendMoveMessage();

                        break;
                }
            }
        };

        // Make a small delay so that the threads have initialized handlers
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            Log.e("GameBoard: ", e.toString());
        }

        updateScreenInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If continuous mode was selected in main menu, send message to the current player thread
        if(currGameMode == MODE_CONT) {
            sendMoveMessage();
        }
    }

    // Send a message to the current player handler to make a move
    private void sendMoveMessage(){
        Message msg;
        if(currPlayer == PLAYER_1){
            msg = smartThread.getHandler().obtainMessage(MAKE_MOVE);
            smartThread.getHandler().sendMessage(msg);
        }else{
            msg = randomThread.getHandler().obtainMessage(MAKE_MOVE);
            randomThread.getHandler().sendMessage(msg);
        }
    }

    // Check if a move passed in by a worker thread is valid
    private boolean checkMove(int r, int c){
        int selectedMove = gameBoardInformation[r][c]; // Info grid value
        int dX = Math.abs(r - gopherLocation[0]); // Difference between gopher and coordinates
        int dY = Math.abs(c - gopherLocation[1]);
        Log.i("CheckMove: ", "(" + r + ", " + c + ") and " + "(" + gopherLocation[0] + ", " + gopherLocation[1] + ") == " + dX + " and " + dY);
        if(selectedMove == GOPHER){ // Success
            gameOver = true;
            // Start intent for winner pop up window activity
            Intent i = new Intent(GameBoard.this, WinnerPopUpWindow.class);
            if(currPlayer == PLAYER_1){ // Player 1 wins
                Toast.makeText(GameBoard.this, "Bender Wins", Toast.LENGTH_SHORT).show();
                i.putExtra(WINNER,"Bender Wins!!!");
            }else{ // Player 2 wins
                Toast.makeText(GameBoard.this, "Jerry Wins", Toast.LENGTH_SHORT).show();
                i.putExtra(WINNER,"Jerry Wins!!!");
            }
            startActivityForResult(i,CONFIRMATION_RESULT);
        }else if(dX <= 1 && dY <= 1){
            if(selectedMove == EMPTY) { // Near Miss
                gameBoardInformation[r][c] = NEAR;
                Toast.makeText(GameBoard.this, "Near Miss!!!", Toast.LENGTH_SHORT).show();
            }else{ // Disaster
                Toast.makeText(GameBoard.this, "Disaster", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if((dX == 2 && dY <= 2) || (dY == 2 && dX <= 2)){
            if(selectedMove == EMPTY) { // Close Guess
                gameBoardInformation[r][c] = CLOSE;
                Toast.makeText(GameBoard.this, "Close Guess!", Toast.LENGTH_SHORT).show();
            }else{ // Disaster
                Toast.makeText(GameBoard.this, "Disaster", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            if(selectedMove == EMPTY) { // Complete Miss
                gameBoardInformation[r][c] = MISS;
                Toast.makeText(GameBoard.this, "Complete Miss", Toast.LENGTH_SHORT).show();
            }else{ // Disaster
                Toast.makeText(GameBoard.this, "Disaster", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONFIRMATION_RESULT)
            finish(); // End the GameBoard Activity and return to main menu
    }

    // Change the player
    private void changePlayer(){
        if(currPlayer == PLAYER_1)
            currPlayer = PLAYER_2;
        else
            currPlayer = PLAYER_1;
    }

    // Change the mode
    private void changeMode(){
        if(currGameMode == MODE_MbM)
            currGameMode = MODE_CONT;
        else
            currGameMode = MODE_MbM;
    }

    // Update UI elements
    private void updateScreenInformation(){
        // Update Player
        if(currPlayer == PLAYER_1)
            currPlayerTxt.setText(R.string.player_bender);
        else
            currPlayerTxt.setText(R.string.player_jerry);

        // Update Current mode
        if(currGameMode == MODE_MbM) {
            currGameModeTxt.setText(R.string.mode_MbM);
            MbMButton.setEnabled(true);
        }else{
            currGameModeTxt.setText(R.string.mode_cont);
            MbMButton.setEnabled(false);
        }
    }

    /*
     * RandomThread Class: Worker thread with handler and looper.
     *
     *   The handler receives a MAKE_MOVE message from the UI Thread
     *   and makes move.
     *
     *   The looper then posts a runnable to the
     *   UI Handler to update the Grid accordingly to the move.
     *
     *   The Class uses a random guessing gaming technique.
     *
     */
    public class RandomThread extends HandlerThread {
        private Handler randomHandler;
        private Message msg;

        public RandomThread(String name) { super(name); }

        public Handler getHandler() { return randomHandler; } // Get the handler

        // Return a random location as their move
        private int[] getMove(){ return new int[]{(int) (Math.random() * 10),(int) (Math.random() * 10)}; }

        @Override
        protected void onLooperPrepared() {
            // Thread handler
            randomHandler = new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    // Handle Message
                    int what = msg.what;
                    switch (what) {
                        case MAKE_MOVE: // Generate a move for the game
                            if(!gameOver){
                                // Post runnable to the UI Handler
                                UIHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Get a random move
                                        int[] move = getMove();

                                        // Check if the move is valid (not a disaster)
                                        if(checkMove(move[0],move[1]))
                                            ((TextView) findViewById(gameBoardViews[move[0]][move[1]])).setBackgroundResource(R.drawable.player_2_icon);

                                        // Reuse message object and send out a PROCESS_MOVE to UI Handler
                                        Message outMsg = UIHandler.obtainMessage(PROCESS_MOVE);
                                        UIHandler.sendMessage(outMsg);
                                        // If in continuous mode, set a delay for the moves to be visible
                                        if(currGameMode == MODE_CONT){
                                            try{
                                                Thread.sleep(1000);
                                            }catch (InterruptedException e){
                                                Log.e("RandomThread: ", e.toString());
                                            }
                                        }
                                    }
                                });
                            }
                            break;
                    }
                }
            };

        }
    }

    /*
     * SmartThread Class: Worker thread with handler and looper.
     *
     *   The handler receives a MAKE_MOVE message from the UI Thread
     *   and makes move.
     *
     *   The looper then posts a runnable to the
     *   UI Handler to update the Grid accordingly to the move.
     *
     *   The Class uses a slightly smarter algorithm where it chooses a
     *   block near any block that is a near miss or a close guess. When
     *   none are found, perform a depth first search algorithm to eventually
     *   win if the other thread hasn't won.
     *
     */
    public class SmartThread extends HandlerThread {
        private Handler smartHandler;
        private Message msg;

        public SmartThread(String name) { super(name); }

        public Handler getHandler() { return smartHandler; } // Get the handler

        // Find a block that is near the gopher
        private int[] findNear(){
            int[] near = {-1,-1};
            // Linear search to get the coordinate
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 10; j++)
                    if(gameBoardInformation[i][j] == NEAR) {
                        near[0] = i;
                        near[1] = j;
                    }
            return near;
        }

        // Find a block that is close by
        private int[] findClose(){
            int[] close = {-1,-1};
            // Linear search to get the coordinate
            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 10; j++)
                    if(gameBoardInformation[i][j] == CLOSE) {
                        close[0] = i;
                        close[1] = j;
                    }
            return close;
        }

        // Find the first open space available
        private int[] findOpen(){
            // Launch Depth First Search Algorithm
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    if(gameBoardInformation[i][j] == EMPTY || gameBoardInformation[i][j] == GOPHER){
                        return new int[]{i,j};
                    }
                }
            }
            return null; // It will never return null
        }

        // Pick a direction based off of a given coordinate
        private int[] pickDirection(int r, int c) {
            int[] dir = {r, c};

            if (r != 0) // Up
                if (gameBoardInformation[r - 1][c] == EMPTY || gameBoardInformation[r - 1][c] == GOPHER) {
                    dir[0] = r - 1;
                    return dir;
                }

            if (r != 0 && c != 9) // Up-Right
                if (gameBoardInformation[r - 1][c + 1] == EMPTY || gameBoardInformation[r - 1][c + 1] == GOPHER) {
                    dir[0] = r - 1;
                    dir[1] = c + 1;
                    return dir;
                }

            if (c != 9) // Right
                if (gameBoardInformation[r][c + 1] == EMPTY || gameBoardInformation[r][c + 1] == GOPHER){
                    dir[1] = c + 1;
                    return dir;
                }

            if (r != 9 && c != 9) // Right-Down
                if (gameBoardInformation[r + 1][c + 1] == EMPTY || gameBoardInformation[r + 1][c + 1] == GOPHER) {
                    dir[0] = r + 1;
                    dir[1] = c + 1;
                    return dir;
                }

            if (r != 9) // Down
                if (gameBoardInformation[r + 1][c] == EMPTY || gameBoardInformation[r + 1][c] == GOPHER){
                    dir[0] = r + 1;
                    return dir;
                }

            if (r != 9 && c != 0) // Down-Left
                if (gameBoardInformation[r + 1][c - 1] == EMPTY || gameBoardInformation[r + 1][c - 1] == GOPHER) {
                    dir[0] = r + 1;
                    dir[1] = c - 1;
                    return dir;
                }

            if(c != 0) // Left
                if (gameBoardInformation[r][c - 1] == EMPTY || gameBoardInformation[r][c - 1] == GOPHER){
                    dir[1] = c - 1;
                    return dir;
                }

            if (r != 0 && c != 0) // Left-Up
                if (gameBoardInformation[r - 1][c - 1] == EMPTY || gameBoardInformation[r - 1][c - 1] == GOPHER) {
                    dir[0] = r - 1;
                    dir[1] = c - 1;
                    return dir;
                }

            // The surrounding holes are non-empty and not the gopher, move to a different block
            return new int[]{-1,-1};
        }

        // Get a move based off the grid information
        private int[] getMove(){
            int[] move = new int[]{-1,-1};
            int[] near = findNear(); // Get nearby coordinate
            int[] close = findClose(); // Get close coordinate
            if(near[0] != -1) // Found near
                move = pickDirection(near[0], near[1]);
            if(move[0] == -1 && move[1] == -1) {
                if (close[0] != -1) // Found close
                    move = pickDirection(close[0], close[1]);
                if (move[0] != -1 && move[1] != -1) // Return close direction
                    return move;
            }else // Return near direction
                return move;
            // Pick next move available
            return findOpen();
        }

        @Override
        protected void onLooperPrepared() {
            // Thread Handler
            smartHandler = new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    // Handle Message
                    int what = msg.what;
                    switch (what) {
                        case MAKE_MOVE: // Generate a move for the game
                            if(!gameOver){
                                // Post runnable to UI Handler
                                UIHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int[] move = getMove(); // Get move

                                        // Check if the move is valid (not a disaster)
                                        if(checkMove(move[0],move[1]))
                                            ((TextView) findViewById(gameBoardViews[move[0]][move[1]])).setBackgroundResource(R.drawable.player_1_icon);

                                        // Reuse message object and send out a PROCESS_MOVE to UI Handler
                                        Message outMsg = UIHandler.obtainMessage(PROCESS_MOVE);
                                        UIHandler.sendMessage(outMsg);
                                        // If in continuous mode, set a delay for the moves to be visible
                                        if(currGameMode == MODE_CONT){
                                            try{
                                                Thread.sleep(1000);
                                            }catch (InterruptedException e){
                                                Log.e("SmartThread: ", e.toString());
                                            }
                                        }
                                    }
                                });
                            }
                            break;
                    }
                }
            };
        }
    }

    // DEBUGGING, print the grid information
    private void printGrid(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(gameBoardInformation[i][j] + " ");
            }
            System.out.println();
        }
    }

}
