package game.screens;

import game.GameManagerRevised;
import game.PlayerRevised;
import game.substates.StatusState;

import java.util.Objects;

public class StatusScreen extends BaseScreen {
    public StatusScreen(GameManagerRevised gameManager) {
        super(gameManager);
    }

    @Override
    public void display() {
        switch (gm.getCurrentStatusState()) {
            case DEFAULT -> {
                displayStats();
            }
            case ADVANCED -> {
                console.clearChatHistory("Displaying detailed stats. Press [A] to exit");
                console.appendToConsole("Dodge chance - " + (gm.formatDouble(player.dodgeChance(), 2)) + "%");
                console.appendToConsole("Successful Block chance - " + (gm.formatDouble(player.getBlockPower(), 2)) + "%");
                console.appendToConsole("Critical hit chance - " + (gm.formatDouble(player.getCritChance(), 1)) + "%");
                console.appendToConsole("[EXP] Multiplier - " + player.getBonusXp());
                console.appendToConsole("Spell damage amplification - " + (gm.formatDouble(player.getSpellPower(), 1)) + "%");
                console.appendToConsole("Mana regeneration (per turn) - " + player.getManaRegen());
            }
            case ATTRIBUTE_UP -> {
                console.clearChatHistory("Upgrade an attribute. [SP] left: " + player.getSp() + ". Type [A] to allocate later.");
                for (PlayerRevised.Attribute attr : PlayerRevised.Attribute.values()) {
                    console.appendToConsole("[" + (attr.ordinal() + 1) + "]" + attr);
                }
            }
        }

    }

    @Override
    public void handleInput(String[] words) {
        this.display();
//        if (words == null) {
//            return;
//        }
        switch (gm.getCurrentStatusState()) {
            case DEFAULT -> {
                handleDefaultInput(words);
            }
            case ATTRIBUTE_UP -> {
                handleAttributeInput(words);
            }

            case ADVANCED -> {
                //Press a to exit
                switch (words[0]){
                    case "a":
                        gm.setCurrentStatusState(StatusState.DEFAULT);
                        this.display();
                        break;
                    default:
                }
            }
        }
    }

    private void handleAttributeUpgrade() {
        if (player.getSp() > 0) {
            gm.setCurrentStatusState(StatusState.ATTRIBUTE_UP);
        } else {
            console.appendToConsole("Nuh uh");
        }
    }

    private void handleDefaultInput(String[] words) {
        switch (words[0]) {
            case "g":
                handleAttributeUpgrade();
                this.display();
                break;
            case "f":
                gm.setCurrentStatusState(StatusState.ADVANCED);
                this.display();
                break;
            case "h":
                gm.statsHelp();
                break;
            case "a":
                //exitStatus();
                gm.setScreen(gm.getPreviousScreen());
                break;
        }
    }

    private void handleAttributeInput(String[] words) {
        int id;
        PlayerRevised.Attribute id2 = null;


        try {
            id = Integer.parseInt(words[0]) - 1;
            if (id <= 5 && id >= 0) {
                for (int i = 0; i < PlayerRevised.Attribute.values().length; i++) {
                    if (id == PlayerRevised.Attribute.values()[i].ordinal()) {
                        id2 = PlayerRevised.Attribute.values()[i];
                        break;
                    }
                }
            } else {
                return;
            }


            if (player.getSp() <= 0) {
//                console.appendToConsole("No [SP] left.");
//                console.appendToConsole("Exiting attribute upgrade screen... \nPress [ENTER] to continue.");
                gm.setCurrentStatusState(StatusState.DEFAULT);
                this.display();
            } else {
                player.changeSp(-1);
                player.increaseBaseAttribute(id2, 1);
                console.appendToConsole(player.getAttributeMessage(Objects.requireNonNull(id2)));

            }

        } catch (NumberFormatException e) {
            switch (words[0]) {
                case "a" -> {
                    gm.setCurrentStatusState(StatusState.DEFAULT);
                    this.display();
                }
                default -> {
                }
            }
        }
    }
    private void displayStats() {
        console.clearChatHistory("Displaying [STATUS], [A] to exit");
        console.appendToConsole("NAME: " + player.getName() + " [LVL: " + player.getLevel() + "] ");
        console.writeToConsole("[HP]: " + player.getCurrentHp() + "/" + player.getMaxHp());
        console.appendToConsole(" [MANA]: " + player.getCurrentMana() + "/" + player.getMaxMana());
        console.writeToConsole("[ATK]: " + player.getAtk());
        console.appendToConsole(" [HP POTIONS]: " + player.getHealthPotionAmount());
        console.writeToConsole("[EXP]: " + player.getExp() + "/" + player.getExpToNext() + " ");
        //exp bar
        // (exp * 100) / exp to next
        //
        short expPercentage = (short) ((short) (player.getExp() * 100) / player.getExpToNext());
        short amount = (short) (expPercentage / 10);
        console.writeToConsole("[");
        for (int i = 0; i < amount; i++) {
            console.writeToConsole("#");
        }
        for (int i = 0; i < 10 - amount; i++) {
            console.writeToConsole("-");
        }
        console.writeToConsole("] [TOTAL EXP]: " + player.getTotalExp());
        console.writeToConsole(" [SKILLPOINTS]: " + player.getSp() + "\n");
        console.writeToConsole("MONEY: ");
        gm.formatMoney(player.getMoney());
        console.appendToConsole("");

//        console.writeToConsole("\nSTRENGTH: ");
//        for (int i = 0; i < player.str; i++) {
//            console.writeToConsole("S");
//        }

        // Print all attributes with their values in the specified format
        for (PlayerRevised.Attribute attr : PlayerRevised.Attribute.values()) {
            String attributeName = attr.name();
            int baseAttrValue = player.getBaseAttribute(attr);
            int attributeValue = player.getAttribute(attr);
            if (baseAttrValue != attributeValue) {
                console.appendToConsole("[" + attributeName + "]: " + baseAttrValue + " ==＞ " + attributeValue);
            } else {
                console.appendToConsole("[" + attributeName + "] " + attributeValue);
            }
        }

        console.appendToConsole("");
        if (player.getSp() > 0) console.appendToConsole("Type [G] to allocate [SKILL POINTS]");
        console.appendToConsole("Type [F] to view detailed stats");

    }
}
