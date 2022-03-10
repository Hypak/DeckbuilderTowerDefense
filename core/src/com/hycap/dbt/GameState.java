package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.buildings.AttackableBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.buildings.CentralBuilding;
import com.hycap.dbt.cards.BuyCard;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.projectiles.EnemyProjectile;
import com.hycap.dbt.projectiles.Projectile;
import com.hycap.dbt.tasks.FastforwardTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    public static GameState gameState;
    public GameScreen.Difficulty difficulty;
    public Map map;
    public Deck deck;
    public GameStatistics gameStats;
    public int baseHandSize;
    public int baseEnergy;
    public int currentEnergy;

    public int gold;
    public int maxGold;
    public int goldPerTurn;
    public boolean blocked;
    public boolean animating;

    public float runSpeed;
    float fastForwardRunSpeed = 4.5f;

    public List<Card> freeCardsPerTurn;

    public List<Enemy> enemies;
    public List<Updatable> updatableBuildings;
    public List<Projectile> projectiles;
    public List<Projectile> projectilesToRemove;
    public List<EnemyProjectile> enemyProjectiles;
    public List<EnemyProjectile> enemyProjectilesToRemove;

    public List<MyParticle> particles;

    public Texture hitMarkTexture;

    public GameState(GameScreen.Difficulty difficulty) {
        this.difficulty = difficulty;
        if (difficulty == GameScreen.Difficulty.EASY) {
            baseHandSize = 6;
            CentralBuilding.energyPerTurn = 4;
        } else if (difficulty == GameScreen.Difficulty.NORMAL) {
            baseHandSize = 6;
            CentralBuilding.energyPerTurn = 3;
        } else if (difficulty == GameScreen.Difficulty.HARD) {
            baseHandSize = 5;
            CentralBuilding.energyPerTurn = 3;
        }
        baseEnergy = 0;
        blocked = false;
        gold = 0;
        maxGold = 0;
        goldPerTurn = 0;
        runSpeed = 1;

        map = new Map(this, difficulty);
        deck = new Deck();
        currentEnergy = baseEnergy;

        gameStats = new GameStatistics(this);

        freeCardsPerTurn = new ArrayList<>();
        BuyCard.shownCardAmount = BuyCard.baseShownCardAmount;

        enemies = new ArrayList<>();
        updatableBuildings = new ArrayList<>();
        projectiles = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();

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
            deck.addToHand(card);
        }
    }

    public void update(float deltaT) {
        boolean attackableBuildingExists = false;
        for (Building building : map.getBuildingList()) {
            if (building instanceof AttackableBuilding) {
                attackableBuildingExists = true;
                break;
            }
        }
        if (!attackableBuildingExists) {
            UIManager.showEndGameUI();
            return;
        }
        if (UIManager.showingMenu) {
            return;
        }
        if (!animating) {
            projectiles = new ArrayList<>();
            enemyProjectiles = new ArrayList<>();
            return;
        }
        animating = false;
        for (Updatable e : updatableBuildings) {
            e.update(deltaT * runSpeed);
            animating |= e.keepActive();
        }
        projectilesToRemove = new ArrayList<>();
        for (Updatable p : projectiles) {
            p.update(deltaT * runSpeed);
            animating |= p.keepActive();
        }
        projectiles.removeAll(projectilesToRemove);
        enemyProjectilesToRemove = new ArrayList<>();
        for (Updatable p : enemyProjectiles) {
            p.update(deltaT * runSpeed);
            animating |= p.keepActive();
        }
        enemyProjectiles.removeAll(enemyProjectilesToRemove);
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
        FastforwardTask.finished = true;
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
