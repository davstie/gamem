package game.enums;

import java.util.*;

// EnemyType.java
public enum EnemyType {
    // Define enemy types here with their base stats
    SKELETON(15, 3, 5, "SKELETON", 1D),
    ZOMBIE(20, 2, 4, "ZOMBIE", 0.85D),
    GOBLIN(10, 4, 3, "GOBLIN", 0.7D),
    WOLF(13, 3, 4, "WOLF", 0.9D),
    GOLEM(25, 4, 8, "GOLEM", 1.5D),
    TREANT(15, 3, 6, "TREANT", 1.2D),
    HORNED_RABBIT(7,1,2,"HORNED RABBIT",0.35D),
    THUG(16,3,5,"THUG",1.05D),
    DUMMY(1, 0, 0, "Dummy", 1D);

    private final int maxHp;
    private final int damage;
    private final int xpReward;
    private final String name;
    private final double rewardModifier;

    // Constructor for enum entries
    EnemyType(int maxHp, int damage, int xpReward, String name, double rewardModifier) {
        this.maxHp = maxHp;
        this.damage = damage;
        this.xpReward = xpReward;
        this.name = name;
        this.rewardModifier = rewardModifier;
    }

    private static final Random RANDOM = new Random();

    public static EnemyType getRandomType(EnemyType... excludedTypes) {
        List<EnemyType> allowedTypes = new ArrayList<>();
        Set<EnemyType> excluded = new HashSet<>(Arrays.asList(excludedTypes));

        excluded.add(EnemyType.DUMMY);

        for (EnemyType type : values()) {
            if (!excluded.contains(type)) {
                allowedTypes.add(type);
            }
        }
        if (allowedTypes.isEmpty()) {
            return EnemyType.DUMMY;
        }

        return allowedTypes.get(RANDOM.nextInt(allowedTypes.size()));
    }

    public static EnemyType getRandomTypeAllowed(EnemyType... allowedTypes) {
        List<EnemyType> allowed = new ArrayList<>(Arrays.asList(allowedTypes));

        if (allowed.isEmpty()) {
            return EnemyType.DUMMY;
        }

        return allowed.get(RANDOM.nextInt(allowed.size()));
    }

    public static final List<EnemyType> FOREST_POOL = List.of(GOBLIN, SKELETON, WOLF,TREANT,ZOMBIE);
    public static final List<EnemyType> CAVE_POOL = List.of(SKELETON, ZOMBIE, GOLEM);
    public static final List<EnemyType> PLAINS_POOL = List.of(WOLF,HORNED_RABBIT);
    public static final List<EnemyType> TOWN_POOL = List.of(GOBLIN, WOLF,HORNED_RABBIT);

    public static EnemyType getRandomFromPool(List<EnemyType> pool) {
        if (pool == null || pool.isEmpty()) {
            return EnemyType.DUMMY;
        }
        return pool.get(RANDOM.nextInt(pool.size()));
    }

    // Getters (no setters, since values are final)
    public int getMaxHp() {
        return maxHp;
    }

    public int getDamage() {
        return damage;
    }

    public int getXpReward() {
        return xpReward;
    }

    public String getName() {
        return name;
    }

    public double getRewardModifier() {
        return rewardModifier;
    }
}