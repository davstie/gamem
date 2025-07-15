package game;

import game.enums.EnemyType;

public class Enemy {
    private int maxHp;
    private int currentHp;
    private int damage;
    private int xpReward;
    private String name;
    private EnemyType currentType;
    private static final int DEFAULT_XP_REWARD = 10;

    public Enemy(EnemyType type){
        this.maxHp = type.getMaxHp();
        this.damage = type.getDamage();
        this.xpReward = type.getXpReward();
        this.name = type.getName();
        this.currentHp = maxHp;
    }

    public void changeType(EnemyType newType) {
        this.maxHp = newType.getMaxHp();
        this.currentHp = newType.getMaxHp();
        this.damage = newType.getDamage();
        this.xpReward = newType.getXpReward();
        this.name = newType.getName();
        this.currentType = newType;

    }

    public int getXpReward(){
        return xpReward;
    }

    public EnemyType getType(){
        return currentType;
    }

    public void damageEnemy(int a){
        currentHp -= a;
    }

    public void changeHp(int a){
        currentHp += a;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(currentHp, maxHp);
    }


    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    public String getName() { return name; }

}
