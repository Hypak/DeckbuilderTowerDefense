package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Renderer {
    public static void draw(Batch batch, Texture texture, float x, float y, float alpha, float scale) {
        Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(scale / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }

    public static void draw(Batch batch, Texture texture, float x, float y, float alpha) {
        Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }

    public void draw(Batch batch, Texture texture, float x, float y) {
        Sprite sprite = new Sprite(texture);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }
}
