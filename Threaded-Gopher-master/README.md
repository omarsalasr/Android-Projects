# Threaded-Gopher
Android application that utilizises multi-threading to play a game of Gopher.

The objective of the game is to guess a square on the screen and hope to find where the Gopher is hidden.
The first person to guess the location wins the game.

There are multiple components to the game.
First, there is a move-by-move mode where a button governs the guessing mechanics of the game, and then there is a guess-by-guess mode where it's continuous guessing until there is a winner.

Both modes have two computer players that are controlled each through their own thread.
When in guess-by-guess, the player sits back and watches how both the threads play against each other to find the Gopher.

There are in total three threads that communicate with each other throughout the match.
1. The UI thread, this controls the UI elements in the screen like change the images and display the winner.
2. Worker thread 1, this is player one that has an intial depth-first search algorithm to find the Gopher. As the game goes on, the worker threads know how close a player gets to the Gopher. This worker thread then starts using that information to make more localized guesses so that he kind find the Gopher much quicker.
3. Worker thread 2, this is player two that has a totally random guessing algorithm where it chooses a random box to guess at.

The worker threads compute these guesses on their own thread so the UI thread isn't bottlnecked. Then, they post a runnable to the UI thread to check and process the move so that the UI thread can make the updates.