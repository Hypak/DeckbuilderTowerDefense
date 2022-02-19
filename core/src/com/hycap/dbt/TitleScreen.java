package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class TitleScreen extends ScreenAdapter {

    Table startTable;
    Stage stage;

    @Override
    public void render(float deltaT) {
        ScreenUtils.clear(230/255f, 240/255f, 255/255f, 1);

        stage.act(deltaT);
        stage.draw();
    }

    @Override
    public void show() {
        stage = new Stage();
        startTable = new Table();
        String[] names = new String[]{"Easy", "Normal", "Hard"};
        for (int i = 0; i < names.length; ++i) {
            final GameScreen.Difficulty difficulty = GameScreen.Difficulty.values()[i];
            Button button = new TextButton("Start " + names[i] + " Game", SkinClass.skin);
            button.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    DBTGame.game.setScreen(new GameScreen(difficulty));
                }
            });
            Record record = DBTGame.recordsHolder.records.get(difficulty);
            Label roundLabel = new Label("Best Round: " + record.mostRounds +
                    " / " + Map.difficultyRadius.get(difficulty), SkinClass.skin);
            Label baseLabel = new Label("Most Bases Destroyed: " + record.mostBasesDestroyed, SkinClass.skin);
            startTable.add(button).pad(15);
            startTable.add(roundLabel).pad(15);
            startTable.add(baseLabel).row();
        }
        stage.addActor(startTable);
        startTable.setFillParent(true);
        startTable.align(Align.center);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
