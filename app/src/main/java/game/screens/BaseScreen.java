
package game.screens;
import game.*;

public abstract class BaseScreen implements Screen {
    protected GameManagerRevised gm;
    protected Console2 console;
    protected PlayerRevised player;
    protected Quests quests;
    protected Enemy enemy;

    public BaseScreen(GameManagerRevised gameManager) {
        this.gm = gameManager;
        this.console = gm.getConsole();
        this.player = gm.getPlayer();
        this.quests = gm.getQuests();
        this.enemy = gm.getEnemy();
    }
}
