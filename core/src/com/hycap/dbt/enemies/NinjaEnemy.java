package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.projectiles.RangedEnemyProjectile;

public class NinjaEnemy extends Enemy {
    public static Texture texture;
    private final float attackTime;
    private final float attackDamage;
    private final float dashDist;
    private final float invisEffectSeconds;
    private final float invisEffectRange;

    enum DashStatus {
        READY,
        DASHING,
        DASHED
    };
    private DashStatus dashStatus;
    private float dashSpeedMult;
    private float timeUntilNextAttack;

    public NinjaEnemy(final Vector2 position) {
        super(position, 0);
        super.buildingTargetPriority = PreferTowers.preferTowers;
        moveSpeed = 1.4f;
        health = 18;
        attackTime = 2f;
        timeUntilNextAttack = 0;
        attackDamage = 50;
        attackRange = 0.75f;
        dashDist = 2f;
        dashSpeedMult = 2.5f;
        invisEffectSeconds = 1.5f;
        invisEffectRange = 3;
        dashStatus = DashStatus.READY;
    }

    @Override
    public void update(final float deltaT) {
        if (health < 0) {
            return;
        }
        timeUntilNextAttack -= deltaT;
        switch (dashStatus) {
            case READY:
                super.update(deltaT);
                if (damageToTake > 0 || targetDist <= attackRange + dashDist) {
                    dashStatus = DashStatus.DASHING;
                    invisSeconds = invisEffectSeconds;
                    moveSpeed *= dashSpeedMult;
                    for (final Enemy enemy : GameState.gameState.enemies) {
                        if (enemy.equals(this)) {
                            continue;
                        }
                        if (enemy.position.dst(position) <= invisEffectRange) {
                            enemy.invisSeconds = invisEffectSeconds;
                        }
                    }
                }
                break;
            case DASHING:
            case DASHED:
                super.update(deltaT);
                break;
        }
        if (targetDist <= attackRange && timeUntilNextAttack <= 0 && targetBuilding != null) {
            invisSeconds = 0;
            targetBuilding.attack(attackDamage);
            timeUntilNextAttack = attackTime;
            if (dashStatus == DashStatus.DASHING) {
                dashStatus = DashStatus.DASHED;
                moveSpeed /= dashSpeedMult;
            }
        }
    }

    @Override
    public Enemy clone() {
        return new NinjaEnemy(new Vector2(position));
    }

    @Override
    public String getName() {
        return "Ninja Enemy";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

}
