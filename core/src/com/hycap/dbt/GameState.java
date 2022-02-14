package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.enemies.BasicEnemy;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState {
    public static GameState gameState;
    public Map map;
    public Deck deck;
    public int baseHandSize;
    public int baseEnergy;
    public int currentEnergy;

    public int gold;
    public int maxGold;
    public int goldPerTurn;
    public boolean blocked;
    public boolean animating;

    public List<Enemy> enemies;
    public List<Updatable> updatables;
    public List<Updatable> updatablesToAdd;
    public List<Updatable> updatablesToRemove;

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

        updatables = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public void newTurn() {
        map.newTurn();
        currentEnergy = baseEnergy;
        gold += goldPerTurn;
        if (gold > maxGold) {
            gold = maxGold;
        }
        animating = true;
        deck.drawNewHand(baseHandSize);
    }

    public void update(float deltaT) {
        if (!animating) {
            return;
        }
        animating = false;
        updatablesToAdd = new ArrayList<>();
        updatablesToRemove = new ArrayList<>();
        for (Updatable u : updatables) {
            u.update(deltaT);
            if (u.keepActive()) {
                animating = true;
            }
        }
        updatables.addAll(updatablesToAdd);
        updatables.removeAll(updatablesToRemove);
    }
}
