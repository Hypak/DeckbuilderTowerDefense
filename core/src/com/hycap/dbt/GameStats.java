package com.hycap.dbt;

public class GameStats {
    int radius;
    int maxRadius;
    int enemyBasesDestroyed;
    int totalEnemyBases;
    int buildingsPlaced;
    int cardsBought;

    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
        DBTGame.recordsHolder.updateBestRound(GameState.gameState.difficulty, radius);
    }
    public int getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
    public int getEnemyBasesDestroyed() {
        return enemyBasesDestroyed;
    }
    public void setEnemyBasesDestroyed(int enemyBasesDestroyed) {
        this.enemyBasesDestroyed = enemyBasesDestroyed;
        DBTGame.recordsHolder.updateMostBases(GameState.gameState.difficulty, enemyBasesDestroyed);
    }
    public void incrementEnemyBasesDestroyed() {
        ++enemyBasesDestroyed;
        DBTGame.recordsHolder.updateMostBases(GameState.gameState.difficulty, enemyBasesDestroyed);
    }

    public int getTotalEnemyBases() {
        return totalEnemyBases;
    }

    public void setTotalEnemyBases(int totalEnemyBases) {
        this.totalEnemyBases = totalEnemyBases;
    }

    public int getBuildingsPlaced() {
        return buildingsPlaced;
    }

    public void setBuildingsPlaced(int buildingsPlaced) {
        this.buildingsPlaced = buildingsPlaced;
    }

    public int getCardsBought() {
        return cardsBought;
    }
    public void setCardsBought(int cardsBought) {
        this.cardsBought = cardsBought;
    }
    public void incrementCardsBought() {
        ++this.cardsBought;
    }


    public GameStats(GameState gameState) {
        radius = gameState.map.currentRadius;
        maxRadius = gameState.map.SIZE / 2;
        enemyBasesDestroyed = 0;
        totalEnemyBases = gameState.map.enemyBases.size();
        buildingsPlaced = 0;
        cardsBought = 0;
    }
}
