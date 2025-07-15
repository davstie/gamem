package game;

public class Statistics {
    private int enemiesKilled = 0;
    private int enemiesKilledTotal = 0;
    private int totalExpGot = 0;

    public void setEnemiesKilled(int amount){
        enemiesKilled = amount;
    }
    public void changeEnemiesKilled(int amount){
        enemiesKilled += amount;
        enemiesKilledTotal += amount;
    }

    public int getEnemiesKilled(){
        return enemiesKilled;
    }

    public void changeTotalExpGot(int amount){
        totalExpGot += amount;
    }
}
