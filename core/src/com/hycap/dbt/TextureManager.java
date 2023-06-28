package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.cards.*;
import com.hycap.dbt.enemies.*;
import com.hycap.dbt.projectiles.EarthquakeProjectile;
import com.hycap.dbt.projectiles.RangedEnemyProjectile;
import com.hycap.dbt.projectiles.SniperProjectile;
import com.hycap.dbt.projectiles.TowerProjectile;
import com.hycap.dbt.units.FarmerUnit;
import com.hycap.dbt.units.KnightUnit;

final class TextureManager {
    public static Texture grassTexture;
    public static Texture riftTexture;
    public static Texture circleTexture;
    public static Texture selectedTexture;
    public static float circleSizeMult;

    private TextureManager() {
    }

    public static void setTextures() {
        CentralBuilding.texture = new Texture("CentralBuilding.png");
        PathBuilding.texture = new Texture("PathBuilding.png");
        PathCard.texture = new Texture("PathCard.png");
        Path0EnergyCard.texture = new Texture("Path0EnergyCard.png");
        MineBuilding.texture = new Texture("MineBuilding.png");
        MineCard.texture = new Texture("MineCard.png");

        ShackCard.texture = new Texture("ShackCard.png");
        ShackBuilding.texture = new Texture("ShackBuilding.png");

        BarracksCard.texture = new Texture("BarracksCard.png");
        BarracksBuilding.texture = new Texture("BarracksBuilding.png");

        FieldBuilding.texture = new Texture("FieldBuilding.png");
        FieldCard.texture = new Texture("FieldCard.png");
        CoffersBuilding.texture = new Texture("CoffersBuilding.png");
        CoffersCard.texture = new Texture("CoffersCard.png");
        MageBuilding.texture = new Texture("MageBuilding.png");
        MageCard.texture = new Texture("MageCard.png");
        PaverBuilding.texture = new Texture("PaverBuilding.png");
        PaverCard.texture = new Texture("PaverCard.png");
        LibraryBuilding.texture = new Texture("LibraryBuilding.png");
        LibraryCard.texture = new Texture("LibraryCard.png");
        TowerBuilding.texture = new Texture("TowerBuilding.png");
        TowerCard.texture = new Texture("TowerCard.png");
        WallBuilding.texture = new Texture("WallBuilding.png");
        WallCard.texture = new Texture("WallCard.png");
        SpikesBuilding.texture = new Texture("SpikesBuilding.png");
        SpikesCard.texture = new Texture("SpikesCard.png");
        SniperBuilding.texture = new Texture("SniperBuilding.png");
        SniperCard.texture = new Texture("SniperCard.png");
        EarthquakeBuilding.texture = new Texture("EarthquakeBuilding.png");
        EarthquakeCard.texture = new Texture("EarthquakeCard.png");

        FarmerUnit.texture = new Texture("FarmerUnit.png");
        KnightUnit.texture = new Texture("KnightUnit.png");

        Draw2Card.texture = new Texture("Draw2Card.png");
        Remove1Card.texture = new Texture("Remove1Card.png");
        BuyCard.texture = new Texture("BuyCard.png");
        Recycle2Card.texture = new Texture("Recycle2Card.png");

        BasicEnemy.texture = new Texture("BasicEnemy.png");
        FastEnemy.texture = new Texture("FastEnemy.png");
        RangedEnemy.texture = new Texture("RangedEnemy.png");
        BigEnemy.texture = new Texture("BigEnemy.png");
        NinjaEnemy.texture = new Texture("NinjaEnemy.png");

        EnemyBase.texture = new Texture("EnemyBase.png");

        TowerProjectile.texture = new Texture("TowerProjectile.png");
        SniperProjectile.texture = new Texture("TowerProjectile.png");
        EarthquakeProjectile.texture = new Texture("TowerProjectile.png");
        RangedEnemyProjectile.texture = new Texture("RangedEnemyProjectile.png");

        grassTexture = new Texture("Grass2.png");
        riftTexture = new Texture("EnergyRift.png");
        circleTexture = new Texture("Circle.png");
        selectedTexture = new Texture("Selected.png");
        circleSizeMult = 32f / 512f;
    }

    public static void draw(final Batch batch, final Texture texture, final float x, final float y, final float alpha, final float scale) {
        final Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(scale / 32f);
        sprite.setPosition(x - texture.getWidth() / 2f, y - texture.getHeight() / 2f);
        sprite.draw(batch);
    }

    public static void draw(final Batch batch, final Texture texture, final float x, final float y, final float alpha) {
        final Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - texture.getWidth() / 2f, y - texture.getHeight() / 2f);
        sprite.draw(batch);
    }

    public static void draw(final Batch batch, final Texture texture, final float x, final float y) {
        final Sprite sprite = new Sprite(texture);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - texture.getWidth() / 2f, y - texture.getHeight() / 2f);
        sprite.draw(batch);
    }
}
