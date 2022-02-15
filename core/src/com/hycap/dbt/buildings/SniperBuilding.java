package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.cards.SniperCard;

public class SniperBuilding extends TowerBuilding {
    public static Texture texture;
    @Override
    public void onCreate(GameState gameState) {
        super.range = 7;
        super.onCreate(gameState);
        super.health = 75;
        super.range = 7;
        super.reloadTime = 3;
        super.damage = 5;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
