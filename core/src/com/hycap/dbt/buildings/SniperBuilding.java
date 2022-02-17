package com.hycap.dbt.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.hycap.dbt.GameState;
import com.hycap.dbt.cards.SniperCard;

public class SniperBuilding extends AbstractTowerBuilding {
    public static Texture texture;
    public SniperBuilding() {
        super.health = 75;
        super.range = 7;
        super.reloadTime = 2.5f;
        super.damage = 5;
    }

    @Override
    public void onCreate(GameState gameState) {

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
