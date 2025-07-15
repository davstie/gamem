package game;

public class Spell {
    private int spellCost = 2;
    private int spellDamage;
    private String spellName;

    public Spell(String spellName , int spellCost, int spellDamage) {
        this.spellName = spellName;
        this.spellCost = spellCost;
        this.spellDamage = spellDamage;
    }



    public int getSpellCost() {
        return spellCost;
    }

    public void setSpellCost(int spellCost) {
        this.spellCost = spellCost;
    }

    public int getSpellDamage() {
        return spellDamage;
    }

    public void setSpellDamage(int spellDamage) {
        this.spellDamage = spellDamage;
    }

    public String getSpellName() {
        return spellName;
    }

    @Override
    public String toString() {
        return getSpellName();
    }
}
