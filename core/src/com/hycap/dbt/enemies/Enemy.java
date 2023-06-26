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
    private static final Sound attackSound;
    private static final Sound dieSound;
    static {
        attackSound = Gdx.audio.newSound(Gdx.files.internal("EnemyHit.ogg"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("EnemyDie.ogg"));
    }


    Vector2 position;
    float moveSpeed;
    private Vector2 target;
    BuildingTargetPriority buildingTargetPriority;
    AttackableBuilding targetBuilding;
    float targetDist;
    float attackRange;
    public float health;
    public float damageToTake;
    public boolean takenDamage = false;
    private float offsetAngle;

    public float invisSeconds = 0;

    private static final Random random;

    static {
        random = new Random();
    }

    Enemy(final Vector2 position) {
        this.position = position;
        float maxOffsetAngle = (float) Math.PI / 8;
        offsetAngle = 2f * (random.nextFloat() - 0.5f) * maxOffsetAngle;
    }

    Enemy(final Vector2 position, final float maxOffsetAngle) {
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

    private void setTargetNearest() {
        float closestDist = Float.MAX_VALUE;
        int highestPriority = Integer.MIN_VALUE;
        for (final Building building : GameState.gameState.map.getBuildingList()) {
            final Pair<Integer> coords = building.getPosition();
            if (!(building instanceof AttackableBuilding)) {
                continue;
            }
            final Vector2 vecCoords = new Vector2(coords.getLeft(), coords.getRight());
            float dist = vecCoords.sub(position).len();
            if (dist < 1) {
                // Always attack close
                dist = Float.MIN_VALUE;
            }
            final float priority = buildingTargetPriority.getPriority(this, building);
            dist -= priority;
            if (dist < closestDist) {
                closestDist = dist;
                target = new Vector2(coords.getLeft(), coords.getRight());
                targetBuilding = (AttackableBuilding)building;
            }
        }
    }

    @Override
    public void update(final float deltaT) {
        if (damageToTake > 0 || takenDamage) {
            offsetAngle = 0;
        }
        if (invisSeconds > 0) {
            invisSeconds -= deltaT;
        }
        setTargetNearest();
        final Vector2 move = new Vector2(target).sub(position);
        targetDist = move.len();
        if (targetDist > attackRange) {
            move.scl(1 / targetDist);
            final Vector2 rotatedMove = move.rotateRad(offsetAngle);
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
