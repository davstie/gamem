package game.screens;

import game.*;
import game.enums.EnemyType;
import game.enums.TownState;

public class TownScreen extends BaseScreen {
    public TownScreen(GameManagerRevised gameManager) {
        super(gameManager);
        this.display();
    }


    @Override
    public void handleInput(String[] words) {
        // The input handling is also tied to the screen object.
        this.display();
        switch (gm.getCurrentTownState()) {
            case Main:
                handleMainTownInput(words);
                break;
            case InGuild:
                handleGuildInput(words);
                break;
            case InShop:
                handleShopInput(words);
                break;
            case InInn:
                handleInnInput(words);
                break;
        }
    }

    @Override
    public void display() {
        // The display logic is now tied to the screen object itself.
        // It checks its own internal state (e.g., in the guild, in the shop)
        // and draws the appropriate UI.
        switch (gm.getCurrentTownState()) {
            case Main:
                displayMainTown();
                break;
            case InGuild:
                displayGuild();
                break;
            case InShop:
                displayShop();
                break;
            case InInn:
                displayInn();
                break;
        }
    }

    private void displayMainTown() {
        console.clearChatHistory("You are in [BEGINNER TOWN]");
        console.appendToConsole("You see a [GUILD] an [INN] and a [SHOP]");
        //      console.appendToConsole("You can also leave to fight a [MONSTER]");
        console.appendToConsole("[1] Go to the [GUILD]");
        console.appendToConsole("[2] Go to the [INN]");
        console.appendToConsole("[3] Go to the [SHOP]");
        console.appendToConsole("[4] Go fight a [MONSTER]");
        console.appendToConsole("[5] Go exploring");
        console.appendToConsole("[STATS] see your stats");
    }

    private void displayGuild() {
        console.clearChatHistory("You are in the [GUILD]. [A] to leave.");
        quests.updateQuestStates();
        gm.formatMoney(player.getMoney());
        console.appendToConsole("");
        for (int i = 0; i < quests.getQuestCount(); i++) {
            final int slot = i + 1;
            Quests.QuestState state = quests.getQuest(slot).getState();
            String name = quests.getQuest(slot).getName();
            console.writeToConsole("[" + (slot) + "]");
            // Quest name details, etc.
            console.writeToConsole(name);


            if (state == Quests.QuestState.AVAILABLE) {
                console.appendToConsole(" STATUS: NOT TAKEN");
            } else if (state == Quests.QuestState.ACTIVE) {
                console.appendToConsole(" STATUS: TAKEN");
            } else if (state == Quests.QuestState.COMPLETED) {
                console.appendToConsole(" STATUS: COMPLETED");
            }

        }
    }

    private void displayShop() {
        console.clearChatHistory("You are in the [SHOP]. [A] to leave.");
        gm.formatMoney(player.getMoney());
        console.appendToConsole("");
        console.appendToConsole("[HP POTION]: " + player.getHealthPotionAmount() + "/" + player.getMaxHealthPotionAmount());
        console.appendToConsole("[1] Buy a [HP POTION] - 1S 35C");
        console.appendToConsole("[2] Buy a [POTION SLOT] (+1 max Potions) - 4S 75C");
    }

    private void displayInn() {
        console.clearChatHistory("You are in the [INN]. [A] to leave.");
        console.appendToConsole("You can rest here to recover your health and mana");
        gm.formatMoney(player.getMoney());
        console.appendToConsole("");
        console.appendToConsole("[HP] - " + player.getCurrentHp() + "/" + player.getMaxHp() + " | [MP] - " + player.getCurrentMana() + "/" + player.getMaxMana());
        console.appendToConsole("[1] Stay - 75C");
        console.appendToConsole("[2] Drink - 15C");
    }

    private void handleMainTownInput(String[] words) {
        switch (words[0]) {
            case "1":
                gm.setCurrentTownState(TownState.InGuild);
                this.display();
                break;
            case "2":
                gm.setCurrentTownState(TownState.InInn);
                this.display();
                break;
            case "3":
                gm.setCurrentTownState(TownState.InShop);
                this.display();
                break;
            case "4":
                gm.setPreviousScreen(this);
                gm.triggerFightFromPool(EnemyType.TOWN_POOL);
                break;
            case "5":
                gm.setPreviousScreen(this);
                gm.setScreen(new WorldScreen(gm));
                break;
            case "stats", "status":
                gm.enterStatus(this);
                break;
        }
    }

    private void handleShopInput(String[] words) {
        switch (words[0]) {
            case "1":
                if (player.getMoney() >= 135 && player.getHealthPotionAmount() < player.getMaxHealthPotionAmount()) {
                    player.changeMoney(-135);
                    player.setHealthPotionAmount((player.getHealthPotionAmount() + 1));
                    this.display();
                    console.appendToConsole("You buy a health potion");
                    break;
                } else if (player.getHealthPotionAmount() == player.getMaxHealthPotionAmount()) {
                    console.appendToConsole("You don't have enough space");
                } else {
                    console.appendToConsole("You don't have enough money");
                }
                break;
            case "2":
                if (player.getMoney() >= 475) {
                    player.changeMoney(-475);
                    player.changeMaxHealthPotionAmount(player.getMaxHealthPotionAmount() + 1);
                    this.display();
                    console.appendToConsole("You buy a potion slot");
                } else {
                    console.appendToConsole("You don't have enough money");
                }
                break;
            case "a":
                gm.setCurrentTownState(TownState.Main);
                console.appendToConsole("You leave the [SHOP]");
                break;

        }
    }

    private void handleGuildInput(String[] words) {
        if (words[0].equals("a")) {
            gm.setCurrentTownState(TownState.Main);
            console.appendToConsole("You leave the [GUILD]");
            this.display();
            return;
        }

        for (int i = 0; i < quests.getQuestCount(); i++) {
            final int slot = i + 1;
            if (words[0].equals(String.valueOf(slot)) && quests.getQuest(slot).getState() == Quests.QuestState.AVAILABLE) {
                EnemyType enemyType = quests.getQuest(slot).getTargetEnemyType();
                String enemyName = enemyType.getName();
                quests.getQuest(slot).setState(Quests.QuestState.ACTIVE);
                this.display();
                console.appendToConsole("You take the quest to kill [" + enemyName + "]");

            } else if (words[0].equals(String.valueOf(slot)) && quests.getQuest(slot).getState() == Quests.QuestState.ACTIVE) {
                console.appendToConsole("You've already taken this quest.");
            } else if (words[0].equals(String.valueOf(slot)) && quests.getQuest(slot).getState() == Quests.QuestState.COMPLETED) {
                int reward = quests.getQuest(slot).getReward();
                player.changeMoney(reward);
                this.display();
                console.appendToConsole("You take the reward.");
                console.appendToConsole("You've got" + Quests.formatReward(quests.getQuest(slot).getReward()));
                gm.newQuest(slot);
            }
        }
    }

    private void handleInnInput(String[] words) {
        long playerMoney = player.getMoney();
        switch (words[0]) {
            case "a":
                gm.setCurrentTownState(TownState.Main);
                this.display();
                break;
            case "1":
                if (playerMoney >= 75) {
                    player.changeMoney(-75);
                    player.changeHealth(player.getMaxHp());
                    player.changeMana(player.getMaxMana());
                    this.display();
                } else {
                    console.appendToConsole("Not enough money.");
                }

                break;
            case "2":
                if (playerMoney >= 15) {
                    player.changeMoney(-15);
                    player.changeHealth(4);
                    player.changeMana(1);
                    player.increaseBaseAttribute(PlayerRevised.Attribute.VITALITY, 1);
                } else {
                    console.appendToConsole("Not enough money.");
                }
                break;
        }
    }
}
