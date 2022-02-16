package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.cards.SniperCard;

public class SniperBuilding extends AbstractTowerBuilding {
    public static Texture texture;

    @Override
    public void onCreate(GameState gameState) {
        super.health = 75;
        super.range = 7;
        super.reloadTime = 2.5f;
        super.damage = 5;
        super.onCreate(gameState);
    }

    @Override
    public Building duplicate() {
        return new SniperBuilding();
    }

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }
}
