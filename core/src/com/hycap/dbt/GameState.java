package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.hycap.dbt.buildings.CentralBuilding;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.enemies.BasicEnemy;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    public float runSpeed;
    float fastForwardRunSpeed = 3;

    public List<Card> freeCardsPerTurn;

    public List<Enemy> enemies;
    public List<Updatable> updatableBuildings;

    public List<MyParticle> particles;

    public Texture hitMarkTexture;

    public GameState() {
        map = new Map();
        deck = new Deck();
        baseHandSize = 6;
        baseEnergy = 3;
        currentEnergy = baseEnergy;
        blocked = false;
        gold = 0;
        maxGold = CentralBuilding.goldCapacity;
        goldPerTurn = 0;
        runSpeed = 1;

        freeCardsPerTurn = new ArrayList<>();

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
        for (Card card : freeCardsPerTurn) {
            deck.getHand().add(card.duplicate());
        }
    }

    public void update(float deltaT) {
        if (!animating) {
            return;
        }
        animating = false;
        for (Updatable e : updatableBuildings) {
            e.update(deltaT * runSpeed);
            animating |= e.keepActive();
        }
        for (Enemy e : enemies) {
            e.update(deltaT * runSpeed);
            animating |= e.keepActive();
        }
    }

    public void toggleFastForward() {
        if (runSpeed == 1) {
            runSpeed = fastForwardRunSpeed;
        } else {
            runSpeed = 1;
        }
    }

    public void addHurtParticle(Vector2 position) {
        float variation = 0.3f;
        Random random = new Random();
        Vector2 particlePos = new Vector2(position);
        particlePos.x += variation * (random.nextFloat() - 0.5f);
        particlePos.y += variation * (random.nextFloat() - 0.5f);
        MyParticle newParticle = new MyParticle(hitMarkTexture, particlePos, 0.25f, true, 0.4f);
        particles.add(newParticle);
    }
}
