package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Map;
import com.hycap.dbt.Pair;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.buildings.AttackableBuilding;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.tasks.KillEnemyTask;

import java.util.Random;

public abstract class Enemy implements Updatable {
    Vector2 position;
    float moveSpeed;
    Vector2 target;
    AttackableBuilding targetBuilding;
    float targetDist;
    float attackRange;
    float health;
    float offsetAngle;
    float maxOffsetAngle = (float)Math.PI / 4;

    public Enemy(Vector2 position) {
        this.position = position;
        offsetAngle = (new Random().nextFloat() - 0.5f) * maxOffsetAngle;
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
        for(Pair<Integer> coords : GameState.gameState.map.getBuildingCoords()) {
            Building building = GameState.gameState.map.getBuilding(coords.getLeft(), coords.getRight());
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
        GameState.gameState.addHurtParticle(position);
        health -= damage;
        if (health <= 0) {
            GameState.gameState.enemies.remove(this);
            KillEnemyTask.finished = true;
        }
    }

    public boolean keepActive() {
        return true;
    }

    public abstract Enemy clone();

    public Texture getTexture() {
        return null;
    }
}
