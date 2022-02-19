package com.hycap.dbt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DBTGame extends Game {
    public static DBTGame game;
    public static RecordsHolder recordsHolder;

    @Override
    public void create() {
        game = this;
        recordsHolder = new RecordsHolder();
        setScreen(new TitleScreen());
    }
}
