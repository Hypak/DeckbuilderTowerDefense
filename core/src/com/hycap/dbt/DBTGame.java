package com.hycap.dbt;

import com.badlogic.gdx.Game;

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
