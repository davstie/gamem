package game;

import java.util.ArrayList;
import java.util.List;

import game.enums.SubStates;

public class FightService {
    List<Spell> spells = initSpells();

    public AttackResult executePlayerAttack(PlayerRevised player, Enemy enemy) {
        int initialHp = enemy.getCurrentHp();
        boolean isCritical = player.isCritical();
        int damageDealt;
        if (isCritical) {
            damageDealt = (int) (Math.round(player.getAtk() * player.getCriticalPower()));
        } else {
            damageDealt = player.getAtk();
        }
        enemy.changeHp(-damageDealt);
        return new AttackResult(damageDealt, isCritical, initialHp, enemy.getCurrentHp());

    }

    public BlockResult executePlayerBlock(PlayerRevised player, Enemy enemy) {
        boolean blockSuccess = player.willTheyBlock();
        if (!blockSuccess) {
            int damageReceived = (int) (Math.round(enemy.getDamage() * ((100 - player.getBlockPower()) / 40)))
                    - player.getBonusDef();
            player.changeHealth(-damageReceived);
            return new BlockResult(false, damageReceived);
        }

        return new BlockResult(true, 0);

    }

    public EnemyAttackResult executeEnemyAttack(PlayerRevised player, Enemy enemy) {
        int damageDealt = enemy.getDamage();
        if (enemy.getCurrentHp() >= 1) {
            player.changeHealth(-damageDealt);
        }
        return new EnemyAttackResult(damageDealt);
    }

    public PotionUseResult executePotionUse(PlayerRevised player) {
        int healingReceived;
        int playerHpPotionsAmount = player.getHealthPotionAmount();

        healingReceived = (int) (player.getMaxHp() * 0.3);
        player.changeMaxHealthPotionAmount(player.getHealthPotionAmount() - 1);
        player.setCurrentHp(player.getCurrentHp() + healingReceived);

        return new PotionUseResult(healingReceived);
    }

    public CastSpellInfo executeSpell(PlayerRevised player, Enemy enemy, int spellIndex) {
        Spell czar = spells.get(spellIndex - 1);

        final int spellCost = czar.getSpellCost();
        final int damage = (int) Math.round(czar.getSpellDamage() * (player.getSpellPower() / 100 + 1));
        final String spellName = czar.getSpellName();

        if (player.getCurrentMana() >= spellCost) {
            SubStates.SpellSelection.setState(false);
            player.changeMana(-spellCost);
            enemy.damageEnemy(damage);
            return new CastSpellInfo(spellName, damage, spellCost);
        } else {
            return null;
        }
    }

    public record AttackResult(int damageDealt, boolean wasCrit, int enemyInitialHp, int enemyFinalHp) {
    }

    public record BlockResult(boolean blockSuccess, int damageReceived) {
    }

    public record EnemyAttackResult(int damageDealt) {
    }

    public record PotionUseResult(int healingReceived) {
    }

    public record CastSpellInfo(String spellName, int damageDealt, int cost) {
    }

    public List<Spell> initSpells() {
        List<Spell> spele = new ArrayList<>();
        spele.add(new Spell("DragonFire", 15, 20));
        spele.add(new Spell("Ice Blizzard", 12, 15));
        spele.add(new Spell("Avalanche", 8, 8));
        return spele;
    }
}
