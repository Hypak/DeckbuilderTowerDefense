package com.hycap.dbt;

import com.badlogic.gdx.math.Vector2;

public interface Attackable {
    void attack(float damage);
    Vector2 getVecPosition();
}
