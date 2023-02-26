package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class SkinClass {
    public static final Skin skin;
    static {
        skin = new Skin(Gdx.files.internal("gdx-skins-master/plain-james/skin/plain-james-ui.json"));
    }

    private SkinClass() {
    }
}
