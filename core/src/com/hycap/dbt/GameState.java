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
    private static final float maxDeltaT = 1/60f;
    private static final int skipSeconds = 1200;

    public static GameState gameState;

    public final GameScreen.Difficulty difficulty;
    public final Map map;
    public final Deck deck;
    public final GameStatistics gameStats;
    private final int baseHandSize;
    public int baseEnergy;
    public int currentEnergy;

    public int gold;
    public int maxGold;
    public int goldPerTurn;
    public boolean blocked;
    boolean animating;

    private RunSpeed runSpeed;
    boolean paused;

    public final List<Card> freeCardsPerTurn;

    public final List<Enemy> enemies;
    final List<EnemyBase> updatableBases;
    public List<Projectile> projectiles;
    public List<Projectile> projectilesToRemove = null;
    public List<EnemyProjectile> enemyProjectiles;
    public List<EnemyProjectile> enemyProjectilesToRemove = null;

    final List<MyParticle> particles;

    private final Texture hitMarkTexture;

    public enum RunSpeed {
        SLOW (1f),
        MEDIUM (3f),
        FAST(9f);
        private final float speed;
        RunSpeed(final float speed) {
            this.speed = speed;
        }
        private float speed() {
            return speed;
        }
    }

    public GameState(final GameScreen.Difficulty difficulty) {
        this.difficulty = difficulty;
        switch (difficulty) {
            case NORMAL:
                baseHandSize = 6;
                CentralBuilding.energyPerTurn = 3;
                break;
            case HARD:
                baseHandSize = 5;
                CentralBuilding.energyPerTurn = 3;
                break;
            case CREATIVE:
                baseHandSize = 10;
                CentralBuilding.energyPerTurn = 100;
                break;
            case EASY:
            default:
                baseHandSize = 6;
                CentralBuilding.energyPerTurn = 4;
                break;
        }
        baseEnergy = 0;
        blocked = false;
        animating = false;
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

    void newTurn() {
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
            projectiles = new ArrayList<>(10);
            enemyProjectiles = new ArrayList<>(10);
            return;
        }
        animating = false;

        deltaT *= runSpeed.speed();
        if (deltaT > maxDeltaT) {
            final int updateCounts = (int)Math.floor(deltaT / maxDeltaT);
            final float updateRemainder = deltaT - updateCounts * maxDeltaT;
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
            for (final Building building : map.getBuildingList()) {
                if (building instanceof AttackableBuilding) {
                    final AttackableBuilding attackableBuilding = (AttackableBuilding)building;
                    attackableBuilding.newTurn();
                }
            }
            UIManager.endAnimating();
        }
    }

    private void performUpdate(final float deltaT) {
        for (final Building building : map.buildingList) {
            if (building instanceof Updatable) {
                ((Updatable)building).update(deltaT);
            }
        }
        for (final Updatable e : updatableBases) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
        projectilesToRemove = new ArrayList<>();
        for (final Updatable p : projectiles) {
            p.update(deltaT);
            animating |= p.keepActive();
        }
        projectiles.removeAll(projectilesToRemove);
        enemyProjectilesToRemove = new ArrayList<>();
        for (final Updatable p : enemyProjectiles) {
            p.update(deltaT);
            animating |= p.keepActive();
        }
        enemyProjectiles.removeAll(enemyProjectilesToRemove);
        for (final Updatable e : enemies) {
            e.update(deltaT);
            animating |= e.keepActive();
        }
    }

    void setRunSpeed(final RunSpeed speed) {
        runSpeed = speed;
        FastforwardTask.finished = true;
    }

    void nextRunSpeed() {
        final RunSpeed[] values = RunSpeed.values();
        for (int i = 0; i < RunSpeed.values().length; ++i) {
            if (values[i] == runSpeed) {
                ++i;
                if (i >= RunSpeed.values().length) {
                    i = 0;
                }
                setRunSpeed(RunSpeed.values()[i]);
                break;
            }
        }
    }

    void skipAnimation() {
        update(skipSeconds);
    }

    public void addHurtParticle(final Vector2 position) {
        final float variation = 0.3f;
        final Random random = new Random();
        final Vector2 particlePos = new Vector2(position);
        particlePos.x += variation * (random.nextFloat() - 0.5f);
        particlePos.y += variation * (random.nextFloat() - 0.5f);
        final MyParticle newParticle = new MyParticle(hitMarkTexture, particlePos, 0.25f, true, 0.4f);
        particles.add(newParticle);
    }
}
