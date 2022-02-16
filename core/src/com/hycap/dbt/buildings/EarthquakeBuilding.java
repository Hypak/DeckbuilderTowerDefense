package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hycap.dbt.GameState;
import com.hycap.dbt.Updatable;
import com.hycap.dbt.cards.BuyableCard;
import com.hycap.dbt.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeBuilding extends AttackableBuilding implements Updatable {
    public static Texture texture;
    float health;
    float damage;
    float reloadTime;
    float timeUntilNextReload;
    float range;
    @Override
    public String getName() {
        return "Earthquake";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onCreate(GameState gameState) {
        super.health = 75;
        damage = 8;
        reloadTime = 1.8f;
        timeUntilNextReload = reloadTime;
        range = 1.6f;
        GameState.gameState.updatableBuildings.add(this);
        super.onCreate(gameState);
    }

    @Override
    public void onDestroy(GameState gameState) {
        GameState.gameState.updatableBuildings.remove(this);
    }

    @Override
    public Building duplicate() {
        return new EarthquakeBuilding();
    }

    @Override
    public void update(float deltaT) {
        timeUntilNextReload -= deltaT;
        if (timeUntilNextReload <= 0) {
            List<Enemy> enemiesToAttack = new ArrayList<>();
            for (Enemy enemy : GameState.gameState.enemies) {
                Vector2 diff = new Vector2(vecPosition).sub(enemy.getPosition());
                float dist = diff.len();
                if (dist < range) {
                    enemiesToAttack.add(enemy);
                }
            }
            if (enemiesToAttack.size() > 0) {
                for (Enemy enemy : enemiesToAttack) {
                    enemy.attack(damage);
                }
                timeUntilNextReload = reloadTime;
            }
        }
    }

    @Override
    public boolean keepActive() {
        return false;
    }
}
