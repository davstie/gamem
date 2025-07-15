package game;

import java.util.HashMap;
import java.util.Map;

public class Equipment {
    // Equipment tags
    public enum EquipmentTag {
        WEAPON, ARMOR, OFFHAND, ACCESSORY
    }

    // Main Attributes
    public enum Attribute {
        STRENGTH, VITALITY, CONSTITUTION, DEXTERITY, LUCK, MANA
    }

    // Effect types
    public enum EffectType {
        ATTACK, DEFENSE, MAGIC
    }

    // Equipment slots
    public enum Slot {
        HEAD, CHEST, LEGS, FEET, MAIN_HAND, OFF_HAND, ARMS, FINGER_1, FINGER_2
    }

    // Item class to represent individual equipment items
    public static class Item {
        private final String name;
        private final EquipmentTag tag;
        private final Map<Attribute, Integer> attributes;
        private final Map<EffectType, Integer> effects;
        private final Slot[] validSlots;

        public Item(String name, EquipmentTag tag, Slot[] validSlots) {
            this.name = name;
            this.tag = tag;
            this.validSlots = validSlots;
            this.attributes = new HashMap<>();
            this.effects = new HashMap<>();

            // Initialize attributes with zero values
            for (Attribute attr : Attribute.values()) {
                attributes.put(attr, 0);
            }
        }

        public String getName() {
            return name;
        }

        public EquipmentTag getTag() {
            return tag;
        }

        public void setAttribute(Attribute attribute, int value) {
            attributes.put(attribute, value);
        }

        public int getAttribute(Attribute attribute) {
            return attributes.getOrDefault(attribute, 0);
        }

        public void setEffect(EffectType effectType, int value) {
            effects.put(effectType, value);
        }

        public int getEffect(EffectType effectType) {
            return effects.getOrDefault(effectType, 0);
        }

        public boolean canEquipInSlot(Slot slot) {
            for (Slot validSlot : validSlots) {
                if (validSlot == slot) {
                    return true;
                }
            }
            return false;
        }

        public Slot[] getValidSlots() {
            return validSlots;
        }
    }

    // Player's equipped items
    private final Map<Slot, Item> equippedItems;
    // All available items
    private final Map<Integer, Item> itemDatabase;
    private int nextItemId = 0;

    public Equipment() {
        equippedItems = new HashMap<>();
        itemDatabase = new HashMap<>();
    }

    // Initialize with some default equipment
    public void initializeEquipment() {
        // Create a sword
        Item sword = new Item("Sword", EquipmentTag.WEAPON, new Slot[]{Slot.MAIN_HAND});
        sword.setEffect(EffectType.ATTACK, 4);
        sword.setAttribute(Attribute.STRENGTH, 2);
        addItem(sword);

        // Create a shield
        Item shield = new Item("Shield", EquipmentTag.OFFHAND, new Slot[]{Slot.OFF_HAND});
        shield.setEffect(EffectType.DEFENSE, 2);
        shield.setAttribute(Attribute.VITALITY, 1);
        addItem(shield);

        // Create some armor
        Item helmet = new Item("Leather Helmet", EquipmentTag.ARMOR, new Slot[]{Slot.HEAD});
        helmet.setEffect(EffectType.DEFENSE, 1);
        helmet.setAttribute(Attribute.CONSTITUTION, 1);
        addItem(helmet);

        Item chestplate = new Item("Chainmail", EquipmentTag.ARMOR, new Slot[]{Slot.CHEST});
        chestplate.setEffect(EffectType.DEFENSE, 3);
        chestplate.setAttribute(Attribute.CONSTITUTION, 2);
        addItem(chestplate);

        Item ring = new Item("Magic Ring", EquipmentTag.ACCESSORY, new Slot[]{Slot.FINGER_1, Slot.FINGER_2});
        ring.setAttribute(Attribute.MANA, 5);
        ring.setEffect(EffectType.MAGIC, 2);
        addItem(ring);

        Item leggings = new Item("Leather Leggings", EquipmentTag.ARMOR, new Slot[]{Slot.LEGS});
        leggings.setEffect(EffectType.DEFENSE, 2);
        leggings.setAttribute(Attribute.CONSTITUTION, 1);
        addItem(leggings);
    }

    // Add an item to the database
    public int addItem(Item item) {
        int itemId = nextItemId++;
        itemDatabase.put(itemId, item);
        return itemId;
    }

    // Get an item from the database
    public Item getItem(int itemId) {
        return itemDatabase.get(itemId);
    }

    // Equip an item to a specific slot
    public boolean equipItem(int itemId, Slot slot) {
        Item item = itemDatabase.get(itemId);
        if (item == null) {
            return false;
        }

        if (!item.canEquipInSlot(slot)) {
            return false;
        }

        equippedItems.put(slot, item);
        return true;
    }
    public boolean equipItemQuick(int itemId) {
        Item item = itemDatabase.get(itemId);
        if (item == null) {
            return false;
        }

        Slot slot = item.getValidSlots()[0];

        equippedItems.put(slot, item);
        return true;
    }

    // Unequip an item from a slot
    public Item unequipSlot(Slot slot) {
        return equippedItems.remove(slot);
    }

    // Check if a slot has an item equipped
    public boolean isSlotEquipped(Slot slot) {
        return equippedItems.containsKey(slot);
    }

    // Get the item equipped in a slot
    public Item getEquippedItem(Slot slot) {
        return equippedItems.get(slot);
    }

    // Apply all equipped items' effects to a player
    public void applyEquipment(PlayerRevised player) {
        // Reset bonus stats
        player.setBonusAtk(0);
        player.setBonusDef(0);

        for (PlayerRevised.Attribute attr : PlayerRevised.Attribute.values()) {
            player.setBonusAttribute(attr, 0);
        }


        // Additional player attribute setters would be needed for full implementation
        // This assumes the PlayerRevised class has these methods

        // Apply effects from all equipped items
        for (Item item : equippedItems.values()) {
            // Apply base effects (attack, defense, magic)
            player.setBonusAtk(player.getBonusAtk() + item.getEffect(EffectType.ATTACK));
            player.setBonusDef(player.getBonusDef() + item.getEffect(EffectType.DEFENSE));

            for (PlayerRevised.Attribute attr : PlayerRevised.Attribute.values()) {
                Equipment.Attribute equipAttr = Equipment.Attribute.valueOf(attr.name());
                player.setBonusAttribute(attr,
                        player.getBonusAttribute(attr) + item.getAttribute(equipAttr));
            }
        }
    }

    // Get all items that can be equipped in a specific slot
    public Map<Integer, Item> getItemsForSlot(Slot slot) {
        Map<Integer, Item> validItems = new HashMap<>();

        for (Map.Entry<Integer, Item> entry : itemDatabase.entrySet()) {
            if (entry.getValue().canEquipInSlot(slot)) {
                validItems.put(entry.getKey(), entry.getValue());
            }
        }

        return validItems;
    }

    // Get the total attribute value from all equipped items
    public int getTotalAttributeBonus(Attribute attribute) {
        int total = 0;
        for (Item item : equippedItems.values()) {
            total += item.getAttribute(attribute);
        }
        return total;
    }
}