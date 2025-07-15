package game.screens;
import game.*;
public class MainMenuScreen extends BaseScreen{

    public MainMenuScreen(GameManagerRevised gameManager) {
        super(gameManager);
        this.display();
    }


    @Override
    public void display() {
        console.clearChatHistory("Main menu v0.04 pre-alpha");
        console.appendToConsole("Type \"Help\" to get the list of available commands.");
    }

    @Override
    public void handleInput(String[] words) {
        this.display();
        gm.setPreviousState();
        switch (words[0]) {
            case "begin", "11":
                gm.setScreen(new WorldScreen(gm));
                break;
            case "5":
//                gm.infiniteFights = !gm.infiniteFights;
//                if (gm.infiniteFights) {
//                    console.appendToConsole("Toggled INFINITE FIGHTS on");
//                } else {
//                    console.appendToConsole("Toggled INFINITE FIGHTS off");
//                }
//                break;
            case "stats", "status":
                gm.enterStatus(this);
                break;
            case "help":
                gm.displayAvailableCmds(1);
                break;
            case "town":
                gm.setScreen(new TownScreen(gm));
                break;
            case "quit":
                console.appendToConsole("Thank you for playing!");
                System.exit(0);
                break;
            default:
                console.appendToConsole("Unknown command.");
        }
    }
}
