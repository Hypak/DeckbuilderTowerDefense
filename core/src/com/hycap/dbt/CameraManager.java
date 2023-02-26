package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.hycap.dbt.tasks.MoveTask;
import com.hycap.dbt.tasks.ScrollTask;

final class CameraManager {
    static OrthographicCamera camera;
    private static final float panSpeed = 15;
    static InputProcessor cameraProcessor;

    private CameraManager() {
    }

    static {
        camera = new OrthographicCamera();
    }

    static void create() {
        camera.setToOrtho(false, 20,
                20f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        resetCamera();
        cameraProcessor = new InputAdapter() {
            @Override
            public boolean scrolled(final float amountX, final float amountY) {
                final double zoomSpeed = 1.1f;
                final float zoomMult = (float) Math.pow(zoomSpeed, amountY);
                camera.zoom *= zoomMult;
                final Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);
                final Vector3 moveVec = new Vector3(camera.position).sub(mousePos);
                moveVec.scl(zoomMult - 1);
                camera.translate(moveVec);
                ScrollTask.finished = true;
                return true;
            }
        };
    }

    static void update() {
        final float move = Gdx.graphics.getDeltaTime() * camera.zoom * panSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += move;
            MoveTask.finished = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= move;
            MoveTask.finished = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += move;
            MoveTask.finished = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= move;
            MoveTask.finished = true;
        }

        GameState.gameState.update(Gdx.graphics.getDeltaTime());

        camera.update();
    }

    static void resetCamera() {
        camera.position.set(GameState.gameState.map.SIZE / 2f, GameState.gameState.map.SIZE / 2f, 0);
        camera.zoom = (GameState.gameState.map.currentRadius + 4) / 6f;
    }

}
