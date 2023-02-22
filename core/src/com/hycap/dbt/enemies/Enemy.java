package com.hycap.dbt.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AttackableBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.tasks.KillEnemyTask;

import java.util.Random;

public abstract class Enemy implements Updatable {
    static Sound attackSound;
    static Sound dieSound;
    static {
        attackSound = Gdx.audio.newSound(Gdx.files.internal("EnemyHit.ogg"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("EnemyDie.ogg"));
    }

    Vector2 position;
    float moveSpeed;
    Vector2 target;
    AttackableBuilding targetBuilding;
    float targetDist;
    float attackRange;
    public float health;
    public float damageToTake;
    float offsetAngle;
    float maxOffsetAngle = (float)Math.PI / 8;

    static Random random;

    static {
        random = new Random();
    }

    public Enemy(Vector2 position) {
        this.position = position;
        offsetAngle = 2f * (random.nextFloat() - 0.5f) * maxOffsetAngle;
    }

    public Enemy(Vector2 position, float maxOffsetAngle) {
        this.position = position;
        offsetAngle = 2f * (random.nextFloat() - 0.5f) * maxOffsetAngle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    void setTargetNearest() {
        float closestSquareDist = Float.MAX_VALUE;
        for (Building building : GameState.gameState.map.getBuildingList()) {
            Pair<Integer> coords = building.getPosition();
            if (!(building instanceof AttackableBuilding)) {
                continue;
            }
            Vector2 vecCoords = new Vector2(coords.getLeft(), coords.getRight());
            float squareDist = vecCoords.sub(position).len2();
            if (squareDist < closestSquareDist) {
                closestSquareDist = squareDist;
                target = new Vector2(coords.getLeft(), coords.getRight());
                targetBuilding = (AttackableBuilding)building;
            }
        }
    }

    @Override
    public void update(float deltaT) {
        setTargetNearest();
        Vector2 move = new Vector2(target).sub(position);
        targetDist = move.len();
        if (targetDist > attackRange) {
            move.scl(1 / targetDist);
            Vector2 rotatedMove = move.rotateRad(offsetAngle);
            position.add(rotatedMove.scl(deltaT * moveSpeed));
        }
    }

    public void attack(float damage) {
        if (health <= 0) {
            return;
        }
        health -= damage;
        GameState.gameState.addHurtParticle(position);
        attackSound.play();
        if (health <= 0) {
            GameState.gameState.enemies.remove(this);
            KillEnemyTask.finished = true;
            dieSound.play();
        }
    }

    public boolean keepActive() {
        return true;
    }

    public abstract Enemy clone();

    public abstract String getName();

    public Texture getTexture() {
        return null;
    }
}
