package com.hycap.dbt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class MyParticle {
    Texture texture;
    Vector2 position;
    float timeLeft;
    float duration;
    boolean fadeOut;
    float scale;

    public MyParticle(Texture texture, Vector2 position, float duration) {
        this.texture = texture;
        this.position = position;
        this.duration = duration;
        this.timeLeft = duration;
        fadeOut = false;
        scale = 1;
    }

    public MyParticle(Texture texture, Vector2 position, float duration, boolean fadeOut, float scale) {
        this.texture = texture;
        this.position = position;
        this.duration = duration;
        this.timeLeft = duration;
        this.fadeOut = fadeOut;
        this.scale = scale;
    }

    public boolean render(Batch batch, float deltaT) {
        if (this.timeLeft <= 0) {
            return false;
        }
        this.timeLeft -= deltaT;
        float alpha = 1;
        if (fadeOut) {
            alpha = timeLeft / duration;
        }
        Renderer.draw(batch, texture, position.x, position.y, alpha, scale);
        return true;
    }
}
