package game;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PlayerRevised {
    // Logger
    private static final Logger logger = Logger.getLogger(PlayerRevised.class.getName());

    // Attribute constants - use enum instead of integer constants
    public enum Attribute {
        STRENGTH, VITALITY, CONSTITUTION, DEXTERITY, LUCK, MANA
    }

    // Base attribute values
    private final Map<Attribute, Integer> baseAttributes = new HashMap<>();

    // Bonus attribute values (from equipment)
    private final Map<Attribute, Integer> bonusAttributes = new HashMap<>();

    // Player stats
    private String name = "Player";
    private int level = 0;
    private int exp = 0;
    private int totalExp = 0;
    private int expToNext = 10;
    private int sp = 0; // Skill points
    private long money = 0;

    // Health and mana
    private int currentHp;
    private int currentMana;

    // Potions
    private int healthPotionAmount = 3;
    private int maxHealthPotionAmount = 3;

    // Equipment bonuses
    private int bonusAtk = 0;
    private int bonusDef = 0;

    // Status flags
    private boolean levelledUp = false;

    /**
     * Creates a new player with specified base attributes
     */
    public PlayerRevised(int strength, int vitality, int constitution, int dexterity, int luck, int mana) {
        // Initialize base attributes
        baseAttributes.put(Attribute.STRENGTH, strength);
        baseAttributes.put(Attribute.VITALITY, vitality);
        baseAttributes.put(Attribute.CONSTITUTION, constitution);
        baseAttributes.put(Attribute.DEXTERITY, dexterity);
        baseAttributes.put(Attribute.LUCK, luck);
        baseAttributes.put(Attribute.MANA, mana);

        // Initialize bonus attributes to zero
        for (Attribute attr : Attribute.values()) {
            bonusAttributes.put(attr, 0);
        }

        // Set initial HP and mana to maximum
        this.currentHp = getMaxHp();
        this.currentMana = getMaxMana();
    }

    /**
     * Gets the total value of an attribute (base and bonus)
     */
    public int getAttribute(Attribute attribute) {
        return baseAttributes.getOrDefault(attribute, 0) + bonusAttributes.getOrDefault(attribute, 0);
    }

    /**
     * Gets the base value of an attribute (without bonuses)
     */
    public int getBaseAttribute(Attribute attribute) {
        return baseAttributes.getOrDefault(attribute, 0);
    }

    /**
     * Gets the bonus value of an attribute (from equipment)
     */
    public int getBonusAttribute(Attribute attribute) {
        return bonusAttributes.getOrDefault(attribute, 0);
    }

    /**
     * Sets the base value of an attribute
     */
    public void setBaseAttribute(Attribute attribute, int value) {
        baseAttributes.put(attribute, value);
    }

    /**
     * Increases the base value of an attribute by the specified amount
     */
    public void increaseBaseAttribute(Attribute attribute, int amount) {
        int currentValue = baseAttributes.getOrDefault(attribute, 0);
        baseAttributes.put(attribute, currentValue + amount);
    }

    /**
     * Sets the bonus value of an attribute (from equipment)
     */
    public void setBonusAttribute(Attribute attribute, int value) {
        bonusAttributes.put(attribute, value);
    }

    /**
     * Returns a message for when an attribute increases
     */
    public String getAttributeMessage(Attribute attribute) {
        return switch (attribute) {
            case STRENGTH -> "You become stronger.";
            case VITALITY -> "Your life force increases.";
            case CONSTITUTION -> "You become more resilient.";
            case DEXTERITY -> "Your reflexes improve.";
            case LUCK -> "You become more fortunate.";
            case MANA -> "Your magical power increases.";
            //default -> "Your abilities improve.";
        };
    }

    // Player name methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Money methods
    public long getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public void changeMoney(int amount) {
        this.money += amount;
    }

    // Skill point methods
    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public void changeSp(int amount) {
        this.sp += amount;
    }

    // Level methods
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean hasLevelledUp() {
        return levelledUp;
    }

    public void resetLevelledUp() {
        this.levelledUp = false;
    }

    // Experience methods
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addExp(int amount) {
        this.exp += amount;
        this.totalExp += amount;
        calculateExp();
    }

    public int getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(int totalExp) {
        this.totalExp = totalExp;
    }

    public int getExpToNext() {
        return expToNext;
    }

    private final float EXP_TO_NEXT_SCALE = 4;
    private final int BASE_EXP_TO_NEXT = 10;

    public void updateExpToNext() {
        this.expToNext = (int) ((level * EXP_TO_NEXT_SCALE) + BASE_EXP_TO_NEXT);
    }

    public void calculateExp() {
        while (exp >= expToNext) {
            levelledUp = true;
            level++;
            exp = exp - expToNext;
            updateExpToNext();
            sp++;
        }
    }

    // Health methods
    private final float MAX_HP_SCALE = 2;
    private final int BASE_MAX_HP = 10;

    public int getMaxHp() {
        return (int) (getAttribute(Attribute.VITALITY) * MAX_HP_SCALE + BASE_MAX_HP);
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(currentHp, getMaxHp());
    }

    public void changeHealth(int amount) {
        this.currentHp = Math.min(this.currentHp + amount, getMaxHp());
    }

    // Mana methods
    private final float MAX_MANA_SCALE = 1.5f;
    private final int MAX_MANA_BASE = 7;

    public int getMaxMana() {
        return (int)(getAttribute(Attribute.MANA) * MAX_MANA_SCALE) + MAX_MANA_BASE;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.min(currentMana, getMaxMana());
    }

    public void changeMana(int amount) {
        this.currentMana = Math.min(this.currentMana + amount, getMaxMana());
    }

    public void regenMana() {
        changeMana(getManaRegen());
    }

    private final float MANA_REGEN_SCALE = 0.5f;
    private final int MANA_REGEN_BASE = 1;

    public int getManaRegen() {
        return (int)(getAttribute(Attribute.MANA) * MANA_REGEN_SCALE) + MANA_REGEN_BASE;
    }

    // Health potion methods
    public int getHealthPotionAmount() {
        return healthPotionAmount;
    }

    public void setHealthPotionAmount(int healthPotionAmount) {
        this.healthPotionAmount = healthPotionAmount;
    }

    public int getMaxHealthPotionAmount() {
        return maxHealthPotionAmount;
    }

    public void changeMaxHealthPotionAmount(int maxHealthPotionAmount) {
        this.maxHealthPotionAmount = maxHealthPotionAmount;
    }

    private final float ATK_SCALE = 0.5f;
    private final int BASE_ATK = 4;

    // Combat stats methods
    public int getAtk() {
        return (int) ((getAttribute(Attribute.STRENGTH) * ATK_SCALE) + bonusAtk)+ BASE_ATK;
    }

    public int getBonusAtk() {
        return bonusAtk;
    }

    public void setBonusAtk(int bonusAtk) {
        this.bonusAtk = bonusAtk;
    }

    public int getBonusDef() {
        return bonusDef;
    }

    public void setBonusDef(int bonusDef) {
        this.bonusDef = bonusDef;
    }

    private final float SPELL_POWER_SCALE_EXPONENT = 1.01f;
    private final int SPELL_POWER_SCALE = 3;

    public double getSpellPower() {
        int mana = getAttribute(Attribute.MANA);
        return Math.pow(mana, SPELL_POWER_SCALE_EXPONENT) * SPELL_POWER_SCALE;
    }

    // Critical hit methods
    public boolean isCritical() {
        return Math.random() < getCritChance() / 100;
    }

    private final float CRIT_CHANCE_SCALE_EXPONENT = 0.896f;
    private final int CRIT_CHANCE_SCALE = 3;
    private final int BASE_MIN_CRIT_CHANCE = 1;
    private final int BASE_MAX_CRIT_CHANCE = 100;

    public double getCritChance() {
        int luck = getAttribute(Attribute.LUCK);
        double chance = Math.min(Math.pow(luck, CRIT_CHANCE_SCALE_EXPONENT) * CRIT_CHANCE_SCALE, BASE_MIN_CRIT_CHANCE);
        return Math.min(chance, BASE_MAX_CRIT_CHANCE);
    }


    public double getCriticalPower() {
        double power = getAttribute(Attribute.STRENGTH) + (getAttribute(Attribute.DEXTERITY) * 3) + getAttribute(Attribute.MANA);
        return (double) Math.round((power / 12) * 100) /100; //Create a multiplier
    }

    // Dodge methods
    public boolean willTheyDodge() {
        return Math.random() < dodgeChance() / 100;
    }


    private final int BASE_MAX_DODGE_CHANCE = 80;
    private final float DODGE_CHANCE_SCALE_EXPONENT = 0.7f;
    private final int BASE_MIN_DODGE_CHANCE = 5;
    private final int DODGE_CHANCE_SCALE = 5;

    public double dodgeChance() {
        int dexterity = getAttribute(Attribute.DEXTERITY);
        double chance = Math.min(Math.pow(dexterity, DODGE_CHANCE_SCALE_EXPONENT) * DODGE_CHANCE_SCALE,BASE_MIN_DODGE_CHANCE);
        return Math.min(chance, BASE_MAX_DODGE_CHANCE);
    }

    // Block methods
    public boolean willTheyBlock() {
        return Math.random() < getBlockPower() / 100;
    }

    private final float BLOCK_POWER_SCALE_EXPONENT = 0.886f;
    private final int BLOCK_POWER_SCALE = 4;
    private final int BASE_MIN_BLOCK_POWER = 5;
    private final int BASE_MAX_BLOCK_POWER = 100;

    public double getBlockPower() {
        int constitution = getAttribute(Attribute.CONSTITUTION);
        double chance = Math.min(Math.pow(constitution, BLOCK_POWER_SCALE_EXPONENT) * BLOCK_POWER_SCALE, BASE_MIN_BLOCK_POWER);
        return Math.min(chance, BASE_MAX_BLOCK_POWER);
    }

    private final float XP_BONUS_SCALING_EXPONENT = 0.95f;
    private final float XP_BONUS_PERCENTAGE = 0.045f;
    // XP bonus method
    public double getBonusXp() {
        double bonus = Math.pow(getAttribute(Attribute.LUCK), XP_BONUS_SCALING_EXPONENT) * XP_BONUS_PERCENTAGE;
        return Math.round(bonus * 100) / 100.0 + 1; //Creates a multiplier and shortens it
    }
}