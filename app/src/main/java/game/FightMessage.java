package game;

public class FightMessage implements Message {
    //GameManagerRevised gm = new GameManagerRevised();

    Console2 console;
    GameManagerRevised gm;
    PlayerRevised player;
    Enemy enemy;

    public FightMessage(Console2 console, PlayerRevised player, Enemy enemy) {
        this.console = console;
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public void execMessages() {
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
}
