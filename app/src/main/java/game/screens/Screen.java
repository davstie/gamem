// In a new file: Screen.java
package game.screens;

import game.GameManagerRevised;

public interface Screen {
    /**
     * Draws the current state of the screen to the console.
     * This is where you clear the console and print all the text.
     */
    void display();

    /**
     * Processes player input specific to this screen.
     * @param words The sanitized, lower-cased input from the player.
     */
    void handleInput(String[] words);
}