package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class CameraManager {
    public static OrthographicCamera camera;
    public static float panSpeed = 15;
    public static InputProcessor cameraProcessor;

    public static void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 20,
                20f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        resetCamera();
        cameraProcessor = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                double zoomSpeed = 1.1f;
                float zoomMult = (float) Math.pow(zoomSpeed, amountY);
                camera.zoom *= zoomMult;
                Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);
                Vector3 moveVec = new Vector3(camera.position).sub(mousePos);
                moveVec.scl(zoomMult - 1);
                camera.translate(moveVec);
                return true;
            }
        };
    }

    public static void update() {
        float move = Gdx.graphics.getDeltaTime() * camera.zoom * panSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y += move;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y -= move;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += move;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= move;
        }

        GameState.gameState.update(Gdx.graphics.getDeltaTime());

        camera.update();
    }

    public static void resetCamera() {
        camera.position.set(GameState.gameState.map.WIDTH / 2f, GameState.gameState.map.WIDTH / 2f, 0);
        camera.zoom = 1;
    }

}
