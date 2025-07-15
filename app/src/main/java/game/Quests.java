package game;

import game.enums.EnemyType;

import java.util.ArrayList;
import java.util.List;

public class Quests {

    /**
     * Represents the state of a quest
     */
    public enum QuestState {
        AVAILABLE(0),
        ACTIVE(1),
        COMPLETED(2);

        private final int value;

        QuestState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static QuestState fromValue(int value) {
            for (QuestState state : QuestState.values()) {
                if (state.getValue() == value) {
                    return state;
                }
            }
            return AVAILABLE; // Default
        }
    }

    /**
     * Represents a single quest
     */
    public static class Quest {
        private final String name;
        private final int killsNeeded;
        private int killsCompleted;
        private final EnemyType targetEnemyType;
        private final int reward;
        private QuestState state;

        public Quest(EnemyType enemyType, int killsNeeded, int reward) {
            this.targetEnemyType = enemyType;
            this.killsNeeded = killsNeeded;
            this.killsCompleted = 0;
            this.reward = reward;
            this.state = QuestState.AVAILABLE;
            this.name = "Kill " + killsNeeded + " [" + enemyType.getName() + "]. Reward:" + formatReward(reward);
        }

        public String getName() {
            return name;
        }

        public int getKillsNeeded() {
            return killsNeeded;
        }

        public int getKillsCompleted() {
            return killsCompleted;
        }

        public EnemyType getTargetEnemyType() {
            return targetEnemyType;
        }

        public int getReward() {
            return reward;
        }

        public QuestState getState() {
            return state;
        }

        public void setState(QuestState state) {
            this.state = state;
        }

        public void addKill(int amount) {
            if (state == QuestState.ACTIVE) {
                killsCompleted += amount;

                // Auto-complete if requirement met
                if (killsCompleted >= killsNeeded) {
                    state = QuestState.COMPLETED;
                }
            }
        }

        public boolean isComplete() {
            return killsCompleted >= killsNeeded;
        }

        public double getProgress() {
            return Math.min(1.0, (double) killsCompleted / killsNeeded);
        }

        @Override
        public String toString() {
            return name + " (" + killsCompleted + "/" + killsNeeded + ") - " + state;
        }
    }

    private final List<Quest> questList;
    private final int maxQuests;

    public Quests() {
        this.maxQuests = 3; // Default size, but could be made configurable
        this.questList = new ArrayList<>(maxQuests);
    }

    /**
     * Initialize with random quests
     */
    public void initQuests() {
        for (int i = 0; i < maxQuests; i++) {
            generateQuest(i + 1);
        }
    }

    /**
     * Generate a new quest for the specified slot (1-based index)
     */
    private final int BASE_REWARD_PER_MONSTER = 35;
    private final int BASE_REWARD = 30;

    public void generateQuest(int slot) {
        int index = slot - 1;

        // Select random enemy type
        EnemyType enemyType = EnemyType.getRandomType();

        // Generate random kill requirement (5-10)
        int killAmount = 5 + (int) (Math.random() * 6);

        // Calculate reward based on enemy type and kill amount
        int reward = (int) ((BASE_REWARD_PER_MONSTER * enemyType.getRewardModifier()) * killAmount) + BASE_REWARD;

        // Create the quest
        Quest newQuest = new Quest(enemyType, killAmount, reward);

        // Add to list or replace existing
        if (index >= questList.size()) {
            questList.add(newQuest);
        } else {
            questList.set(index, newQuest);
        }
    }

    /**
     * Update quest state for a specific slot
     */
    public void setQuestState(int slot, QuestState state) {
        int index = slot - 1;
        if (index >= 0 && index < questList.size()) {
            questList.get(index).setState(state);
        }
    }

    /**
     * Format a money amount into plat, gold, silver, and copper
     */
    public static String formatReward(int amount) {
        int copper = amount;
        int silver = copper / 100;
        int gold = silver / 100;
        int platinum = gold / 100;
        copper = copper % 100;
        silver = silver % 100;
        gold = gold % 100;


        StringBuilder message = new StringBuilder();
        if (platinum > 0) {
            message.append(platinum).append("P");
        }
        if (gold > 0) {
            message.append(" ").append(gold).append("G");
        }
        if (silver > 0) {
            message.append(" ").append(silver).append("S");
        }
        if (copper > 0) {
            message.append(" ").append(copper).append("C");
        }
        return message.toString();
    }

    /**
     * Update all quests when an enemy of the specified type is killed
     */
    public void updateQuestsOnKill(EnemyType enemyType) {
        for (Quest quest : questList) {
            if (quest.getTargetEnemyType() == enemyType && quest.getState() == QuestState.ACTIVE) {
                quest.addKill(1);
            }
        }
    }

    /**
     * Update all quest states based on completion status
     */
    public void updateQuestStates() {
        for (Quest quest : questList) {
            if (quest.getState() == QuestState.ACTIVE && quest.isComplete()) {
                quest.setState(QuestState.COMPLETED);
            }
        }
    }

    /**
     * Get a specific quest
     */
    public Quest getQuest(int slot) {
        int index = slot - 1;
        if (index >= 0 && index < questList.size()) {
            return questList.get(index);
        }
        return null;
    }

    /**
     * Get the total number of quests
     */
    public int getQuestCount() {
        return questList.size();
    }

    /**
     * Get all active quests
     */
    public List<Quest> getActiveQuests() {
        List<Quest> activeQuests = new ArrayList<>();
        for (Quest quest : questList) {
            if (quest.getState() == QuestState.ACTIVE) {
                activeQuests.add(quest);
            }
        }
        return activeQuests;
    }

    /**
     * Get all completed quests that haven't been turned in yet
     */
    public List<Quest> getCompletedQuests() {
        List<Quest> completedQuests = new ArrayList<>();
        for (Quest quest : questList) {
            if (quest.getState() == QuestState.COMPLETED) {
                completedQuests.add(quest);
            }
        }
        return completedQuests;
    }

    /**
     * Get all available quests
     */
    public List<Quest> getAvailableQuests() {
        List<Quest> availableQuests = new ArrayList<>();
        for (Quest quest : questList) {
            if (quest.getState() == QuestState.AVAILABLE) {
                availableQuests.add(quest);
            }
        }
        return availableQuests;
    }
}