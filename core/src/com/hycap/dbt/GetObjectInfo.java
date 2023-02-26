package com.hycap.dbt;

import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.Upgradable;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.ExhaustCard;
import com.hycap.dbt.enemies.Enemy;

final class GetObjectInfo {
    private GetObjectInfo() {
    }

    static String getInfo(final Card card) {
        final StringBuilder string = new StringBuilder();
        string.append(card.getName()).append("\n");
        string.append(card.getInfo());
        if (card instanceof ExhaustCard) {
            string.append("\nRemove after playing.");
        }
        return string.toString();
    }

    static String getInfo(final Building building) {
        final StringBuilder string = new StringBuilder();
        if (building instanceof Upgradable) {
            final Upgradable upgradable = (Upgradable) building;
            string.append("Level ").append(upgradable.getUpgradeLevel()).append(" ");
        }
        string.append(building.getName()).append("\n");
        string.append(building.getInfo()).append("\n");
        string.append(building.getStats());
        return string.toString();
    }

    static String getInfo(final EnemyBase base) {
        final int radius = GameState.gameState.map.getRadius(base.position.getLeft(), base.position.getRight());
        int turnsUntilInRange = radius - GameState.gameState.map.currentRadius;
        if (turnsUntilInRange < 0) {
            turnsUntilInRange = 0;
        }
        final StringBuilder string = new StringBuilder();
        final int turnsUntilNextSpawn = base.turnsUntilNextSpawn + turnsUntilInRange;
        string.append("Next spawn in ").append(turnsUntilNextSpawn);
        if (turnsUntilNextSpawn == 1) {
            string.append(" turn\n");
        } else {
            string.append(" turns\n");
        }
        string.append("Spawns:\n");
        for (final Enemy enemy : base.enemySpawns) {
            string.append(enemy.getName()).append("\n");
        }
        final int turnsUntilUpgrade = base.turnsUntilUpgrade + turnsUntilInRange;
        string.append("Next upgrade in ").append(turnsUntilUpgrade);
        if (turnsUntilUpgrade == 1) {
            string.append(" turn\n");
        } else {
            string.append(" turns\n");
        }
        return string.toString();
    }
}
