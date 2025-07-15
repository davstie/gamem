package game.screens;

import game.GameManagerRevised;

public class WorldScreen extends BaseScreen{

    public WorldScreen(GameManagerRevised gameManager) {
        super(gameManager);
    }

    @Override
    public void display() {
        console.clearChatHistory("Choose your destination. [A] to go back");
        console.appendToConsole("[1] [BEGINNER TOWN]");
        console.appendToConsole("[2] [FOREST]");
        console.appendToConsole("[3] [RUINS] :D ");
    }

    @Override
    public void handleInput(String[] words) {
        this.display();
        switch (words[0]) {
            case "a":
                gm.setScreen(gm.getPreviousScreen());
                break;
            case "1":
                gm.setPreviousScreen(this);
                gm.setScreen(new TownScreen(gm));
                break;
            case "2":
                gm.setPreviousScreen(this);
                gm.setScreen(new ForestScreen(gm));
                break;
            case "status", "stats":
                gm.enterStatus(this);
                break;


        }
    }

}
