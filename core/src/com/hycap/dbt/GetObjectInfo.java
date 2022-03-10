package com.hycap.dbt;

import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.ExhaustCard;
import com.hycap.dbt.enemies.Enemy;

public class GetObjectInfo {
    public static String getInfo(Card card) {
        StringBuilder string = new StringBuilder();
        string.append(card.getName()).append("\n");
        string.append(card.getInfo());
        if (card instanceof ExhaustCard) {
            string.append("\nRemove after playing.");
        }
        return string.toString();
    }

    public static String getInfo(Building building) {
        StringBuilder string = new StringBuilder();
        string.append(building.getName()).append("\n");
        string.append(building.getInfo()).append("\n");
        string.append(building.getStats());
        return string.toString();
    }

    public static String getInfo(EnemyBase base) {
        int radius = GameState.gameState.map.getRadius(base.position.getLeft(), base.position.getRight());
        int turnsUntilInRange = radius - GameState.gameState.map.currentRadius;
        if (turnsUntilInRange < 0) {
            turnsUntilInRange = 0;
        }
        StringBuilder string = new StringBuilder();
        int turnsUntilNextSpawn = base.turnsUntilNextSpawn + turnsUntilInRange;
        string.append("Next spawn in ").append(turnsUntilNextSpawn);
        if (turnsUntilNextSpawn == 1) {
            string.append(" turn\n");
        } else {
            string.append(" turns\n");
        }
        string.append("Spawns:\n");
        for (Enemy enemy : base.enemySpawns) {
            string.append(enemy.getName()).append("\n");
        }
        int turnsUntilUpgrade = base.turnsUntilUpgrade + turnsUntilInRange;
        string.append("Next upgrade in ").append(turnsUntilUpgrade);
        if (turnsUntilUpgrade == 1) {
            string.append(" turn\n");
        } else {
            string.append(" turns\n");
        }
        return string.toString();
    }
}
