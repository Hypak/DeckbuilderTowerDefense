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
    public static float maxDeltaT = 1/600f;

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

    public RunSpeed runSpeed;
    public boolean paused;

    public List<Card> freeCardsPerTurn;

    public List<Enemy> enemies;
    public List<EnemyBase> updatableBases;
    public List<Projectile> projectiles;
    public List<Projectile> projectilesToRemove;
    public List<EnemyProjectile> enemyProjectiles;
    public List<EnemyProjectile> enemyProjectilesToRemove;

    public List<MyParticle> particles;

    public Texture hitMarkTexture;

    public enum RunSpeed {
        SLOW (1f),
        MEDIUM (3f),
        FAST(9f);
        private final float speed;
        RunSpeed(float speed) {
            this.speed = speed;
        }
        private float speed() {
            return speed;
        }
    }

    public GameState(GameScreen.Difficulty difficulty) {
        this.difficulty = difficulty;
        switch (difficulty) {
            case EASY:
                baseHandSize = 6;
                CentralBuilding.energyPerTurn = 4;
                break;
            case NORMAL:
                baseHandSize = 6;
                CentralBuilding.energyPerTurn = 3;
                break;
            case HARD:
                baseHandSize = 5;
                CentralBuilding.energyPerTurn = 3;
                break;
        }
        baseEnergy = 0;
        blocked = false;
        gold = 0;
        maxGold = 0;
        goldPerTurn = 0;
        runSpeed = RunSpeed.SLOW;
        paused = false;

        map = new Map(this, difficulty);
        deck = new Deck();
        currentEnergy = baseEnergy;

        gameStats = new GameStatistics(this);

        freeCardsPerTurn = new ArrayList<>();
        BuyCard.shownCardAmount = BuyCard.baseShownCardAmount;

        enemies = new ArrayList<>();
        updatableBases = new ArrayList<>();
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
        UIManager.startAnimating();
        deck.drawNewHand(baseHandSize);
        deck.addToHand(freeCardsPerTurn);
    }

    public void update(float deltaT) {
        if (paused || UIManager.showingMenu) {
            return;
        }
        if (map.areAllBuildingsDead()) {
            UIManager.showEndGameUI();
            return;
        }
        if (!animating) {
            projectiles = new ArrayList<>();
            enemyProjectiles = new ArrayList<>();
            return;
        }
        animating = false;

        deltaT *= runSpeed.speed();
        if (deltaT > maxDeltaT) {
            int updateCounts = (int)Math.floor(deltaT / maxDeltaT);
            float updateRemainder = deltaT - updateCounts * maxDeltaT;
            for (int i = 0; i < updateCounts; ++i) {
                performUpdate(maxDeltaT);
                if (!animating || map.areAllBuildingsDead()) {
                    animating = false;
                    break;
                }
            }
            if (updateRemainder > 0 && animating) {
                performUpdate(updateRemainder);
            }
        } else {
            performUpdate(deltaT);
        }

        if (!animating) {
            for (Building building : map.getBuildingList()) {
                if (building instanceof AttackableBuilding) {
                    AttackableBuilding attackableBuilding = (AttackableBuilding)building;
                    attackableBuilding.newTurn();
                }
            }
            UIManager.endAnimating();
        }
    }

    private void performUpdate(float deltaT) {
        for (Building building : map.buildingList) {
            if (building instanceof Updatable) {
                ((Updatable)building).update(deltaT);
            }
        }
        for (Updatable e : updatableBases) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
        projectilesToRemove = new ArrayList<>();
        for (Updatable p : projectiles) {
            p.update(deltaT);
            animating |= p.keepActive();
        }
        projectiles.removeAll(projectilesToRemove);
        enemyProjectilesToRemove = new ArrayList<>();
        for (Updatable p : enemyProjectiles) {
            p.update(deltaT);
            animating |= p.keepActive();
        }
        enemyProjectiles.removeAll(enemyProjectilesToRemove);
        for (Updatable e : enemies) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
    }

    public void setRunSpeed(RunSpeed speed) {
        runSpeed = speed;
        FastforwardTask.finished = true;
    }

    public void nextRunSpeed() {
        for (int i = 0; i < RunSpeed.values().length; ++i) {
            if (RunSpeed.values()[i] == runSpeed) {
                ++i;
                if (i >= RunSpeed.values().length) {
                    i = 0;
                }
                setRunSpeed(RunSpeed.values()[i]);
                break;
            }
        }
    }

    public void skipAnimation() {
        // Hopefully 20 minutes is long enough
        update(1200);
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
