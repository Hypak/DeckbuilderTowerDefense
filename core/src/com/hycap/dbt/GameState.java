package com.hycap.dbt;

public class GameState {
    public Map map;
    public Deck deck;
    int baseHandSize;
    int baseEnergy;
    int currentEnergy;

    public int gold;
    public int maxGold;
    public int goldPerTurn;
    public boolean blocked;

    public GameState() {
        map = new Map();
        deck = new Deck();
        baseHandSize = 5;
        baseEnergy = 3;
        currentEnergy = baseEnergy;
        blocked = false;
        gold = 0;
        maxGold = 0;
        goldPerTurn = 0;
    }

    public void newTurn() {
        currentEnergy = baseEnergy;
        gold += goldPerTurn;
        if (gold > maxGold) {
            gold = maxGold;
        }
        deck.drawNewHand(baseHandSize);
    }
}
