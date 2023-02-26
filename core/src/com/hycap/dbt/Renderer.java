package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Renderer {
    public static void draw(final Batch batch, final Texture texture, final float x, final float y, final float alpha, final float scale) {
        final Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(scale / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }

    public static void draw(final Batch batch, final Texture texture, final float x, final float y, final float alpha) {
        final Sprite sprite = new Sprite(texture);
        sprite.setAlpha(alpha);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }

    public void draw(final Batch batch, final Texture texture, final float x, final float y) {
        final Sprite sprite = new Sprite(texture);
        sprite.setScale(1 / 32f);
        sprite.setPosition(x - 16, y - 16);
        sprite.draw(batch);
    }
}
