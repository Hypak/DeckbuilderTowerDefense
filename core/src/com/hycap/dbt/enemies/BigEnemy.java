package com.hycap.dbt.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.SetRenderScale;

public class BigEnemy extends BasicEnemy implements SetRenderScale {
    public static Texture texture;
    public BigEnemy(Vector2 position) {
        super(position);
        super.moveSpeed = 0.75f;
        super.health = 40;
        super.attackTime = 1f;
        super.attackDamage = 20;
        super.attackRange = 0.75f;
    }

    @Override
    public Enemy clone() {
        return new BigEnemy(new Vector2(position));
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "Big Enemy";
    }

    @Override
    public float genRenderScale() {
        return 1.5f;
    }
}
