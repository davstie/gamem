package game.screens;

import game.GameManagerRevised;

public class RewardScreen extends BaseScreen {
    public RewardScreen(GameManagerRevised gameManager) {
        super(gameManager);
    }
    // Opens after a fight to display received rewards better,

    @Override
    public void display() {
        console.clearChatHistory("Rewards");
        int xp = gm.getXpReward();
        console.appendToConsole("You won!");
        console.appendToConsole("You've got: " + xp + " EXP");
        console.appendToConsole("EXP: " + (player.getExp() - xp) + " --> " + player.getExp());
        console.appendToConsole("Total EXP: " + (player.getTotalExp() - xp) + " --> " + player.getTotalExp() + "\n");
        if (player.hasLevelledUp()) {
            int currentLevel = player.getLevel();
            int previousLevel = gm.getPreviousLevel();
            int difference = currentLevel - previousLevel;
            if (difference > 1) {
                console.appendToConsole("You've levelled up " + difference + " times!");
            } else {
                console.appendToConsole("You've levelled up!");
            }
            console.appendToConsole("You have " + player.getSp() + " SP");
        }
        console.appendToConsole("\nPress [ENTER] to continue...");
    }

    @Override
    public void handleInput(String[] words) {
        if (words[0].isEmpty()) {
            player.resetLevelledUp();
            gm.setScreen(gm.getPreviousScreen());
        }
    }
}
