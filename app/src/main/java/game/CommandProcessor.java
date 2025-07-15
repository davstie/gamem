package game;

public class CommandProcessor {
    private final GameManagerRevised gm;
    private final Console2 console;
    private final PlayerRevised player;

    private static final boolean DEBUG_MODE = true;

    public CommandProcessor(GameManagerRevised gameManagerRevised) {
        this.gm = gameManagerRevised;
        this.console = gameManagerRevised.getConsole();
        this.player = gm.getPlayer();
    }

    public void processCommand(String rawCommand) {
        // 1. Handle "Continue" (empty input) - Let the current screen decide what to do.
        if (rawCommand.trim().isEmpty()) {
            gm.getScreen().handleInput(new String[]{""}); // Send an empty command
            return;
        }

        String[] words = rawCommand.toLowerCase().trim().split("\\s+");

        // 2. Handle Debug Commands (if enabled)
        if (words.length > 1 && DEBUG_MODE) {
            handleDebugCommand(words);
            return;
        }

        // 3. Handle Normal Player Commands
        // The CommandProcessor's ONLY job is to pass the input to the current screen.
        gm.getScreen().handleInput(words);
    }



    private void handleDebugCommand(String[] words) {
        switch (words[0]) {
            case "restore" -> {
                switch (words[1]) {
                    case "hp":
                        player.changeHealth(player.getMaxHp());
                        break;
                    case "mp":
                        player.changeMana(player.getMaxMana());
                        break;
                    case "b":
                        player.changeMana(player.getMaxMana());
                        player.changeHealth(player.getMaxHp());
                        break;
                    default:
                        console.appendToConsole("Wrong argument");
                }
            }

            case "bxp" -> {
                try {
                    int bxp = Integer.parseInt(words[1]);
                    console.appendToConsole("bonus xp is now: " + bxp);
                    gm.changeBonusXpDebug(bxp);
                } catch (NumberFormatException e) {
                    console.appendToConsole("Second arg is not an integer");
                }
            }
            case "screen" -> {

            }
            case "att" -> {
                gm.player.setBaseAttribute(PlayerRevised.Attribute.valueOf(words[1]) , Integer.parseInt(words[2]));
                console.appendToConsole("Attribute " + PlayerRevised.Attribute.STRENGTH + " is now " + gm.player.getAttribute(PlayerRevised.Attribute.STRENGTH));
            }
            case "sp" -> {
                try {
                    console.appendToConsole(String.valueOf(player.getSp()));
                    player.changeSp(Integer.parseInt(words[1]));
                    console.appendToConsole(String.valueOf(player.getSp()));
                } catch (NumberFormatException e) {
                    console.appendToConsole("Requires an int");
                }
            }
            case "gmoney" -> {
                player.changeMoney(Integer.parseInt(words[1]));
                console.appendToConsole("Added " + words[1] + " coins");
            }
            case "equip" -> {
                gm.equipment.equipItemQuick(Integer.parseInt(words[1]));
                console.appendToConsole("Set item " + gm.equipment.getItem(Integer.parseInt(words[1])).getName());
                gm.equipment.applyEquipment(player);
            }
            case "pl"->{
                switch (words[1]){
                    case "blockchance" ->{
                    console.appendToConsole(String.valueOf((player.willTheyBlock())));
                    }
                    case "expmult" ->{
                        console.appendToConsole(String.valueOf(player.getBonusXp()));
                    }
                    case "critmult" ->{
                        console.appendToConsole(String.valueOf(player.getCriticalPower()));
                    }
                }

            }
            case "money"->{
                player.setMoney(Integer.parseInt(words[1]));
            }

            default -> console.appendToConsole("Non-existing command.");

        }

    }


}
