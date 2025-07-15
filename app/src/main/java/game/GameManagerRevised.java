package game;

import game.enums.EnemyType;
import game.enums.TownState;
import game.screens.*;
import game.substates.StatusState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameManagerRevised {


    private final Console2 console;
    Enemy enemy = new Enemy(EnemyType.DUMMY);
    PlayerRevised player = new PlayerRevised(1, 1, 1, 1, 1, 1);
    Equipment equipment = new Equipment();

    Statistics statistics = new Statistics();
    Quests quests = new Quests();

    List<EnemyType> allowed;

    HashMap<String, Message> msgs = null;
    private boolean enemyIsDead = false;
    private boolean startingMethodsCalled = false;
    private boolean lost = false;

    private int bonusXpDebug = 0;
    private int xpReward;
    private int previousLevel;
    private TownState currentTownState = TownState.Main;
    private StatusState currentStatusState = StatusState.DEFAULT;
    //New screen thing
    private Screen currentScreen;
    private Screen previousScreen;

    public GameManagerRevised(Console2 console) {
        this.console = console;
        setScreen(new MainMenuScreen(this));
        startingMethods();
        //enemyFight();
    }

    //For rewards use
    public int getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(int previousLevel) {
        this.previousLevel = previousLevel;
    }

    public Screen getScreen() {
        return this.currentScreen;
    }

    public void setScreen(Screen newScreen) {
        this.currentScreen = newScreen;
        this.currentScreen.display();
    }

    public void startingMethods() {
        if (!startingMethodsCalled) {
            player.changeHealth(player.getMaxHp());
            player.changeMana(player.getMaxMana());
            msgs = initMessages();
            player.changeSp(1);
            quests.initQuests();
            processCommand("");
            equipment.initializeEquipment();
            equipment.applyEquipment(player);
            startingMethodsCalled = true;

        }
    }

    public void changeBonusXpDebug(int bonusXpDebug) {
        this.bonusXpDebug = bonusXpDebug;
    }

    private HashMap<String, Message> initMessages() {
        HashMap<String, Message> msgs = new HashMap<>();
        msgs.put("FightMsg", new FightMessage(console, player, enemy));
        return msgs;
    }

    public void processCommand(String command) {
        new CommandProcessor(this).processCommand(command);
    }

    public Console2 getConsole() {
        return console;
    }


    public void triggerFight(boolean isDefaultSelection) {
        EnemyType type = isDefaultSelection ? EnemyType.getRandomType(EnemyType.DUMMY) : EnemyType.getRandomTypeAllowed(allowed.toArray(new EnemyType[0]));
        enemy.changeType(type);
        setScreen(new FightScreen(this));
    }

    public void triggerFightFromPool(List<EnemyType> pool) {
        EnemyType type = EnemyType.getRandomFromPool(pool);
        enemy.changeType(type);
        setScreen(new FightScreen(this));
    }

    public void allowedEnemyTypes(EnemyType... allowedTypes) {
        allowed = new ArrayList<>(Arrays.asList(allowedTypes));
    }


    public String formatDouble(double num, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return String.valueOf(Math.floor(num * scale) / scale);
    }

    public void statsHelp() {
        console.clearChatHistory("Attributes description ");
        console.appendToConsole("Strength - Affects how much physical damage you deal");
        console.appendToConsole("Vitality - Increases your maximum [HP]");
        console.appendToConsole("Dexterity - Greatly increases crit damage and dodge chance");
        console.appendToConsole("Constitution - Reduces damage taken and increases block chance");
        console.appendToConsole("Mana - Increases spell damage, max [MP] and mana regenerated");
        console.appendToConsole("Luck - Improves crit chance and [EXP] gain");
        console.appendToConsole("===================");
        console.appendToConsole("[ATK] - Estimated attack damage based on attributes");
        console.appendToConsole("[HP POTIONS] - Restore 30% of max [HP]");
    }

    public void displayAvailableCmds(int a) {
        if (a == 1) {
            console.appendToConsole("You are currently in the main menu.");
            console.appendToConsole("Type [11] to begin the game I guess,");
            console.appendToConsole("[STATS] or [STATUS] to see your status or");
            console.appendToConsole("[QUIT] to exit the game.");
        } else if (a == 2) {
            console.appendToConsole("You are in a [FIGHT]");

        }


    }


    public void calcDeath() {
        if (player.getCurrentHp() < 1) {
            // Player death
            console.appendToConsole("You've lost...");

            lost = true;
            player.setCurrentHp(0);
            System.exit(0);

        }

        if (enemy.getCurrentHp() < 1) {
            //Enemy death
            enemy.setCurrentHp(0);
            setPreviousLevel(player.getLevel());

            statistics.changeEnemiesKilled(1);
            enemyIsDead = true;
            // Quests
            quests.updateQuestsOnKill(enemy.getType());


            //int pEXP = player.getExp();
            xpReward = (int) (enemy.getXpReward() * (player.getBonusXp()) + bonusXpDebug);

            player.setExp(player.getExp() + xpReward);
            player.setTotalExp(player.getTotalExp() + xpReward);

            //console.appendToConsole("EXP: " + pEXP + " --> " + player.getExp());
            //player.calculateExp();
            //enemy.setDamage(enemy.getDamage() + 1);

            handleLevelUps(false);
            setScreen(new RewardScreen(this));

            /*
            console.appendToConsole("You've won!");
            console.appendToConsole("Press [ENTER] to continue");
            */


        }

    }

    public void handleLevelUps(boolean message) {

        player.calculateExp();
        if (message) {
            if (player.hasLevelledUp()) {
                console.appendToConsole("You've levelled up!");
                console.appendToConsole("Your level is now: " + player.getLevel());
                console.appendToConsole("[SKILLPOINTS]: " + player.getSp());
                player.resetLevelledUp();
            }
        }


    }

    public void enemyAttack() {
        if (!enemyIsDead && !lost) {
            console.appendToConsole("The enemy attacks!");

            if (player.willTheyDodge()) {
                console.appendToConsole("You've dodged the attack!");
            } else {
                int playerCurrentHp = player.getCurrentHp();

                player.changeHealth((int) -(Math.round(enemy.getDamage() * ((100 - player.getBlockPower()) / 200))));
                player.changeHealth(player.getBonusDef() / 2);

                int damageDealt = playerCurrentHp - player.getCurrentHp();

                console.appendToConsole("They've dealt: " + damageDealt + " damage. YOUR HP: " + playerCurrentHp + " --> " + player.getCurrentHp());
                regenMana();
            }
        }
    }

    public void regenMana() {
        console.appendToConsole("You've regenerated " + player.getManaRegen() + "[MP]");
        player.regenMana();
    }

    public void formatMoney(long amount) {
        StringBuilder message = new StringBuilder();
        if (amount != 0) {
            long copper = amount;
            long silver = copper / 100;
            long gold = silver / 100;
            long platinum = gold / 100;
            copper = copper % 100;
            silver = silver % 100;
            gold = gold % 100;



            if (platinum > 0) {
                message.append(platinum).append("P");
            }
            if (gold > 0) {
                message.append(" ").append(gold).append("G");
            }
            if (silver > 0) {
                message.append(" ").append(silver).append("S");
            }
            if (copper > 0) {
                message.append(" ").append(copper).append("C");
            }
        } else {
            message.append("Broke");
        }
        console.writeToConsole(String.valueOf(message));
    }

    public <T> void writeBar(String s1, T attribute) {
        int attribute1 = (int) attribute;
        s1 = s1.toUpperCase();
        console.writeToConsole("\n[" + s1 + "]: ");
        console.writeToConsole(String.valueOf(attribute1));
//        String firstLetter = s1.substring(0, 1);
//        if (attribute1 <= 15) {
//            for (int i = 0; i < attribute1; i++) {
//                console.writeToConsole(firstLetter);
//            }
//        } else {
//            console.writeToConsole(firstLetter + "x");
//            console.writeToConsole(String.valueOf(attribute1));
//        }


    }

    public PlayerRevised getPlayer() {
        return player;
    }

    public TownState getCurrentTownState() {
        return currentTownState;
    }

    public void setCurrentTownState(TownState currentTownState) {
        this.currentTownState = currentTownState;
    }

    public void setPreviousState() {
    }

    public void newQuest(int slot) {
        quests.generateQuest(slot);
    }

    public Quests getQuests() {
        return quests;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public StatusState getCurrentStatusState() {
        return currentStatusState;
    }

    public void setCurrentStatusState(StatusState currentStatusState) {
        this.currentStatusState = currentStatusState;
    }

    public Screen getPreviousScreen() {
        return previousScreen;
    }

    public void setPreviousScreen(Screen screen) {
        previousScreen = screen;
    }

    public void enterStatus(Screen sc) {
        setPreviousScreen(sc);
        setScreen(new StatusScreen(this));
    }

    public int getXpReward() {
        return xpReward;
    }

}
