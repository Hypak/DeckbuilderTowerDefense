package com.hycap.dbt;

public class GameStatistics {
    private int radius;
    private int maxRadius;
    int enemyBasesDestroyed;
    int totalEnemyBases;
    int buildingsPlaced;
    int cardsBought;

    public int getRadius() {
        return radius;
    }
    void setRadius(final int radius) {
        this.radius = radius;
        DBTGame.recordsHolder.updateBestRound(GameState.gameState.difficulty, radius);
    }
    public int getMaxRadius() {
        return maxRadius;
    }
    public void setMaxRadius(final int maxRadius) {
        this.maxRadius = maxRadius;
    }
    public int getEnemyBasesDestroyed() {
        return enemyBasesDestroyed;
    }
    public void setEnemyBasesDestroyed(final int enemyBasesDestroyed) {
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

    public void setTotalEnemyBases(final int totalEnemyBases) {
        this.totalEnemyBases = totalEnemyBases;
    }

    public int getBuildingsPlaced() {
        return buildingsPlaced;
    }

    public void setBuildingsPlaced(final int buildingsPlaced) {
        this.buildingsPlaced = buildingsPlaced;
    }

    public int getCardsBought() {
        return cardsBought;
    }
    public void setCardsBought(final int cardsBought) {
        this.cardsBought = cardsBought;
    }
    public void incrementCardsBought() {
        ++cardsBought;
    }


    GameStatistics(final GameState gameState) {
        radius = gameState.map.currentRadius;
        maxRadius = gameState.map.SIZE / 2;
        enemyBasesDestroyed = 0;
        totalEnemyBases = gameState.map.enemyBaseManager.enemyBases.size();
        buildingsPlaced = 0;
        cardsBought = 0;
    }
}
