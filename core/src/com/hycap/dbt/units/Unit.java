package com.hycap.dbt.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Pair;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AttackableBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.enemies.BuildingTargetPriority;
import com.hycap.dbt.enemies.Enemy;

import java.util.Random;

public abstract class Unit implements Updatable {
    private static final Sound attackSound;
    private static final Sound dieSound;
    static {
        attackSound = Gdx.audio.newSound(Gdx.files.internal("EnemyHit.ogg"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("EnemyDie.ogg"));
    }


    public Vector2 position;
    float moveSpeed;
    BuildingTargetPriority buildingTargetPriority;
    Enemy targetEnemy;
    float targetDist;
    float attackRange;
    public float health;
    public float damageToTake;
    public boolean takenDamage = false;

    private float offsetAngle;

    private static final Random random;

    static {
        random = new Random();
    }

    public Unit(final Vector2 position) {
        this.position = position;
        final float maxOffsetAngle = (float) Math.PI / 16;
        offsetAngle = 2f * (random.nextFloat() - 0.5f) * maxOffsetAngle;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    private void setTargetNearest() {
        float closestDist = Float.MAX_VALUE;
        targetEnemy = null;
        for (final Enemy enemy : GameState.gameState.enemies) {
            float dist = new Vector2(enemy.getPosition()).sub(position).len();
            if (dist < closestDist) {
                closestDist = dist;
                targetEnemy = enemy;
            }
        }
    }

    @Override
    public void update(final float deltaT) {
        if (health <= 0) {
            return;
        }
        setTargetNearest();
        if (targetEnemy == null) {
            return;
        }
        Vector2 targetVec = new Vector2(targetEnemy.getPosition());
        Vector2 targetVelocity = new Vector2(targetEnemy.velocity);
        final Vector2 directMove = new Vector2(targetVec).sub(position);
        targetDist = directMove.len();
        float targetTime = targetDist / moveSpeed;
        final Vector2 predictiveMove = new Vector2(targetVec).add(targetVelocity.scl(targetTime)).sub(position);
        if (targetDist > attackRange) {
            predictiveMove.scl(1 / targetDist);
            final Vector2 rotatedMove = predictiveMove.rotateRad(offsetAngle);
            position.add(rotatedMove.scl(deltaT * moveSpeed));
        }
    }

    public void attack(final float damage) {
        if (health <= 0) {
            return;
        }
        health -= damage;
        takenDamage = true;
        GameState.gameState.addHurtParticle(position);
        attackSound.play();
        if (health <= 0) {
            GameState.gameState.units.remove(this);
            dieSound.play();
        }
    }

    public boolean keepActive() {
        return true;
    }

    public abstract Unit clone();

    public abstract String getName();

    public Texture getTexture() {
        return null;
    }
}
