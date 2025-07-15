package game.screens;

import game.FightService;
import game.GameManagerRevised;
import game.Spell;
import game.enums.SubStates;

import java.util.List;

public class FightScreen extends BaseScreen {

    private final FightService fightService;
    private final List<Spell> spells;
    private boolean isCastingSpell = false;

    public FightScreen(GameManagerRevised gameManager) {
        super(gameManager);
        this.fightService = new FightService();
        this.spells = fightService.initSpells();
    }

    @Override
    public void display() {
        if (isCastingSpell) {
            spellMessages();
            return;
        }
        console.clearChatHistory("You are in a fight!");
        console.appendToConsole("Type [1] to attack, [2] to block,");
        console.appendToConsole("[3] to drink an HP potion and [4] to begin casting spells.");
        console.appendToConsole(player.getName());
        console.writeToConsole("[HP]: " + player.getCurrentHp() + "/" + player.getMaxHp());
        console.writeToConsole(" [MP]:" + player.getCurrentMana() + "/" + player.getMaxMana());
        console.appendToConsole(" [HP POTION]: " + player.getHealthPotionAmount() + " [ATK]: " + player.getAtk());
        console.appendToConsole("=============================");
        console.appendToConsole(enemy.getName());
        console.appendToConsole("[HP]: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + " [ATK]: " + enemy.getDamage());
        console.appendToConsole("=============================");
    }

    @Override
    public void handleInput(String[] words) {
        this.display();
        if (isCastingSpell) {
            spellSelection(words);
            return;
        }
        switch (words[0]) {
            case "1" -> {
                FightService.AttackResult playerAttackResult = fightService.executePlayerAttack(player, enemy);
                gm.regenMana();
                this.display();
                console.appendToConsole("You attack...");
                if (playerAttackResult.wasCrit()) {
                    console.appendToConsole("Critical hit!");
                }
                if (enemy.getCurrentHp() >= 1) {
                    FightService.EnemyAttackResult enemyAttackResult = fightService.executeEnemyAttack(player, enemy);
                    console.appendToConsole("You dealt " + playerAttackResult.damageDealt() + " damage. " + "ENEMY HP: " + playerAttackResult.enemyInitialHp() + " --> " + playerAttackResult.enemyFinalHp());
                    console.appendToConsole("The enemy attacks... \nThey dealt " + enemyAttackResult.damageDealt() + " damage. PLAYER HP: " + (player.getCurrentHp() + enemyAttackResult.damageDealt() + " --> " + player.getCurrentHp()));
                }
                else{
                    console.appendToConsole("You dealt " + playerAttackResult.damageDealt() + " damage. The enemy has been slain.");
                }                 
                gm.calcDeath();
            }
            case "2" -> {
                FightService.BlockResult blockResult = fightService.executePlayerBlock(player, enemy);
                this.display();
                if (blockResult.blockSuccess()) {
                    console.appendToConsole("Block successful!");
                    console.appendToConsole("The enemy hasn't dealt any damage!");
                } else {
                    console.appendToConsole("You block...");
                    console.appendToConsole("The enemy has dealt: " + blockResult.damageReceived() + " Damage." + " You now have: " + (player.getCurrentHp()) + " Health.");
                }
                gm.calcDeath();
                gm.regenMana();


            }
            case "3" -> {
                if (player.getHealthPotionAmount() < 1) {
                    console.appendToConsole("You don't have any HP potions!");
                } else{
                    FightService.PotionUseResult potionUseResult = fightService.executePotionUse(player);
                    this.display();
                    console.appendToConsole("You drink the HP potion...");
                    console.appendToConsole("You have recovered " + potionUseResult.healingReceived() + " HP!");
                }

            }
            case "4" -> {
                setCastingSpell(true);
                this.display();
            }
            case "help" -> gm.displayAvailableCmds(2);
            case "stats", "status" -> {
            }
            default -> console.appendToConsole("Choose an action");
        }
    }

    public void spellSelection(String[] words) {
        try {
            int i = Integer.parseInt(words[0]);
            if (i > spells.size() || i <= 0) {
                console.appendToConsole("Non-existent spell");
            } else {
                this.display();
                FightService.CastSpellInfo castSpellInfo = fightService.executeSpell(player, enemy, i);

                if (castSpellInfo == null) {
                    console.appendToConsole("Not enough mana.");
                } else {
                    setCastingSpell(false);
                    this.display();
                    console.appendToConsole("You cast [" + castSpellInfo.spellName() + "]");
                    console.appendToConsole("Mana: " + (player.getCurrentMana() + castSpellInfo.cost()) + " --> " + player.getCurrentMana());
                    int damage = castSpellInfo.damageDealt();

                    int ehp = enemy.getCurrentHp(); // Get enemy HP AFTER spell damage

                    if (ehp < 1) {
                        console.appendToConsole("You've dealt " + damage + " damage." + " The enemy has been defeated!");
                    } else {
                        FightService.EnemyAttackResult enemyAttackResult = fightService.executeEnemyAttack(player, enemy);
                        console.appendToConsole("You've dealt " + damage + " damage." + " ENEMY HP: " + (ehp + damage) + " --> " + ehp);
                        console.appendToConsole("The enemy attacks...");
                        console.appendToConsole("They dealt " + enemyAttackResult.damageDealt() + " damage.");
                    }

                    //Enemy attack
                    gm.calcDeath();
                }
            }
        } catch (NumberFormatException e) {
            if (words[0].equals("a") || words[0].equals("exit")) {
                SubStates.SpellSelection.setState(false);
                this.display();
            }
        }
    }

    private void spellMessages() {
        console.clearChatHistory("Spell casting. Type [EXIT] or [A] to go back.");
        for (int i = 0; i < spells.size(); i++) {
            console.writeToConsole("[" + (i + 1) + "] " + spells.get(i).getSpellName());
            console.appendToConsole(" [DAMAGE]: " + (displaySpellDamage(i)) + " [COST]: " + spells.get(i).getSpellCost());
        }
        console.appendToConsole("[MP]: " + player.getCurrentMana() + "/" + player.getMaxMana());
    }

    private String displaySpellDamage(int index) {
        String one = gm.formatDouble((spells.get(index).getSpellDamage() * (player.getSpellPower() / 100 + 1)), 0);
        return one.substring(0, one.length() - 2);
    }

    private void setCastingSpell(boolean castingSpell) {
        isCastingSpell = castingSpell;
    }
}
