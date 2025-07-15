package game.screens;

import game.enums.EnemyType;
import game.GameManagerRevised;

public class ForestScreen extends BaseScreen{
    public ForestScreen(GameManagerRevised gameManager) {
        super(gameManager);
    }

    @Override
    public void display() {
        console.clearChatHistory("You are in the [FOREST]. [A] to see your destinations.");
        //displayPlayerHpAndMp();
        console.appendToConsole("[1] Fight a [MONSTER]");
        console.appendToConsole("[2] Forage for [FOOD] - +2 HP + 1MP");
    }

    @Override
    public void handleInput(String[] words) {
        this.display();
        switch (words[0]) {
            case "a":
                gm.setScreen(gm.getPreviousScreen());
                break;
            case "1":
                gm.triggerFightFromPool(EnemyType.FOREST_POOL);
                gm.setPreviousScreen(this);
                break;
            case "2":
                player.changeHealth(2);
                player.changeMana(1);
                this.display();
                //console.appendToConsole("You foraged for food");
                break;
            case "stats", "status":
                gm.enterStatus(this);
                break;
        }
    }
}
