package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    public List<Updatable> updatableBuildings;

    public List<MyParticle> particles;

    public Texture hitMarkTexture;

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

        enemies = new ArrayList<>();
        updatableBuildings = new ArrayList<>();

        particles = new ArrayList<>();
        hitMarkTexture = new Texture("HitMark.png");
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
        for (Updatable e : updatableBuildings) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
        for (Enemy e : enemies) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
    }

    public void addHurtParticle(Vector2 position) {
        MyParticle newParticle = new MyParticle(hitMarkTexture, position, 0.25f, true, 0.4f);
        particles.add(newParticle);
    }
}
