package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

class MyParticle {
    private final Texture texture;
    private final Vector2 position;
    private float timeLeft;
    private final float duration;
    private final boolean fadeOut;
    private final float scale;

    MyParticle(final Texture texture, final Vector2 position, final float duration, final boolean fadeOut, final float scale) {
        this.texture = texture;
        this.position = position;
        this.duration = duration;
        timeLeft = duration;
        this.fadeOut = fadeOut;
        this.scale = scale;
    }

    boolean render(final Batch batch, final float deltaT) {
        if (timeLeft <= 0) {
            return false;
        }
        timeLeft -= deltaT;
        float alpha = 1;
        if (fadeOut) {
            alpha = timeLeft / duration;
        }
        Renderer.draw(batch, texture, position.x, position.y, alpha, scale);
        return true;
    }
}
