package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.enemies.BasicEnemy;
import com.hycap.dbt.enemies.BigEnemy;
import com.hycap.dbt.enemies.Enemy;
import com.hycap.dbt.enemies.FastEnemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyBaseManager {
    public final List<EnemyBase> enemyBases;
    private final int enemyBaseCount;
    private final int fastEnemyBaseCount;
    private final int bigEnemyBaseCount;
    private final List<Pair<Integer>> setBasicBaseRadii;
    private final List<Pair<Integer>> setFastBaseRadii;
    private final List<Pair<Integer>> setBigBaseRadii;
    private final Map map;

    EnemyBaseManager(final GameScreen.Difficulty difficulty, final Map map) {
        this.map = map;
        if (difficulty == GameScreen.Difficulty.EASY) {
            enemyBaseCount = 30;
            fastEnemyBaseCount = 8;
            bigEnemyBaseCount = 0;
        } else if (difficulty == GameScreen.Difficulty.NORMAL) {
            enemyBaseCount = 75;
            fastEnemyBaseCount = 30;
            bigEnemyBaseCount = 30;
        } else {
            enemyBaseCount = 115;
            fastEnemyBaseCount = 45;
            bigEnemyBaseCount = 45;
        }
        setBasicBaseRadii = new ArrayList<>();
        setBasicBaseRadii.add(new Pair<>(8, 1));
        setBasicBaseRadii.add(new Pair<>(10, 1));
        setBasicBaseRadii.add(new Pair<>(12, 1));
        setBasicBaseRadii.add(new Pair<>(14, 1));
        setBasicBaseRadii.add(new Pair<>(16, 2));
        setBasicBaseRadii.add(new Pair<>(17, 2));
        setBasicBaseRadii.add(new Pair<>(18, 2));

        setFastBaseRadii = new ArrayList<>();
        setFastBaseRadii.add(new Pair<>(20, 1));
        setFastBaseRadii.add(new Pair<>(22, 1));
        setFastBaseRadii.add(new Pair<>(24, 2));
        setFastBaseRadii.add(new Pair<>(26, 2));

        setBigBaseRadii = new ArrayList<>();
        setBigBaseRadii.add(new Pair<>(30, 1));
        setBigBaseRadii.add(new Pair<>(32, 1));
        setBigBaseRadii.add(new Pair<>(34, 1));

        enemyBases = new ArrayList<>();
        generateBases();
    }

    private void generateBases() {
        final Random random = new Random();
        generateSetBases();
        for (int i = 0; i < enemyBaseCount - setBasicBaseRadii.size(); ++i) {
            final int x = random.nextInt(map.SIZE);
            final int y = random.nextInt(map.SIZE);
            final int noBaseRadius = 18;
            if (Math.abs(x - map.SIZE / 2) <= noBaseRadius && Math.abs(y - map.SIZE / 2) <= noBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBasicEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < fastEnemyBaseCount; ++i) {
            final int x = random.nextInt(map.SIZE);
            final int y = random.nextInt(map.SIZE);
            final int noFastBaseRadius = 26;
            if (Math.abs(x - map.SIZE / 2) <= noFastBaseRadius && Math.abs(y - map.SIZE / 2) <= noFastBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateFastEnemyBaseAt(x, y, 2);
        }
        for (int i = 0; i < bigEnemyBaseCount; ++i) {
            final int x = random.nextInt(map.SIZE);
            final int y = random.nextInt(map.SIZE);
            final int noBigBaseRadius = 35;
            if (Math.abs(x - map.SIZE / 2) <= noBigBaseRadius && Math.abs(y - map.SIZE / 2) <= noBigBaseRadius || isEnemyBaseAt(x, y)) {
                --i;
                continue;
            }
            generateBigEnemyBaseAt(x, y, 1);
        }
    }

    private Pair<Integer> getCoordsAtRadius(final int radius) {
        final Random random = new Random();
        final boolean onX = random.nextBoolean();
        final boolean positive = random.nextBoolean();
        int x, y;
        if (positive) {
            x = radius;
        } else {
            x = -radius;
        }
        y = random.nextInt(2 * radius + 1) - radius;
        if (!onX) {
            // Swap x and y
            final int tmp = x;
            x = y;
            y = tmp;
        }
        x += map.SIZE / 2;
        y += map.SIZE / 2;
        return new Pair<>(x, y);
    }

    private void generateSetBases() {
        for (final Pair<Integer> radiusCount : setBasicBaseRadii) {
            final Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateBasicEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
        }
        for (final Pair<Integer> radiusCount : setFastBaseRadii) {
            final Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateFastEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
        }
        for (final Pair<Integer> radiusCount : setBigBaseRadii) {
            final Pair<Integer> coords = getCoordsAtRadius(radiusCount.getLeft());
            generateBigEnemyBaseAt(coords.getLeft(), coords.getRight(), radiusCount.getRight());
        }
    }

    private void generateBasicEnemyBaseAt(final int x, final int y, final int spawnCount) {
        final List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new BasicEnemy(new Vector2(x, y)));
        }
        final EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 1.5f);
        enemyBases.add(newBase);
    }

    private void generateFastEnemyBaseAt(final int x, final int y, final int spawnCount) {
        final List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new FastEnemy(new Vector2(x, y)));
        }
        final EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 2f);
        enemyBases.add(newBase);
    }

    private void generateBigEnemyBaseAt(final int x, final int y, final int spawnCount) {
        final List<Enemy> enemySpawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; ++i) {
            enemySpawns.add(new BigEnemy(new Vector2(x, y)));
        }
        final EnemyBase newBase = new EnemyBase(new Pair<>(x, y), enemySpawns, 2f);
        enemyBases.add(newBase);
    }

    private boolean isEnemyBaseAt(final int x, final int y) {
        final Pair<Integer> pos = new Pair<>(x, y);
        for (final EnemyBase base : enemyBases) {
            if (base.position.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    EnemyBase getEnemyBaseAt(final int x, final int y) {
        final Pair<Integer> pos = new Pair<>(x, y);
        for (final EnemyBase base : enemyBases) {
            if (base.position.equals(pos)) {
                return base;
            }
        }
        return null;
    }

    void newTurn() {
        for (final EnemyBase base : enemyBases) {
            if (map.isInRadius(base.position.getLeft(), base.position.getRight())) {
                base.startTurn();
                if (!GameState.gameState.updatableBases.contains(base)) {
                    GameState.gameState.updatableBases.add(base);
                }
            }
        }
    }

    int getNextBaseRadius() {
        int nextRadius = Integer.MAX_VALUE;
        for (final EnemyBase base : enemyBases) {
            final int radius = GameState.gameState.map.getRadius(base.position.getLeft(), base.position.getRight());
            if (radius > GameState.gameState.map.currentRadius && radius < nextRadius) {
                nextRadius = radius;
            }
        }
        if (nextRadius == Integer.MAX_VALUE) {
            nextRadius = 0;  // Nicer fail value than MAX_VALUE
        }
        return nextRadius;
    }

    int getEnemyCountNextWave() {
        int enemyCount = 0;
        for (final EnemyBase base : enemyBases) {
            final int radius = GameState.gameState.map.getRadius(base.position.getLeft(), base.position.getRight());
            if (radius <= GameState.gameState.map.currentRadius + 1 && base.turnsUntilNextSpawn <= 1) {
                enemyCount += base.enemySpawns.size();
            }
        }
        return enemyCount;
    }

    public void destroyEnemyBase(final EnemyBase base) {
        enemyBases.remove(base);
        GameState.gameState.map.riftCoords.add(new Pair<>(base.position.getLeft(), base.position.getRight()));
    }
}
