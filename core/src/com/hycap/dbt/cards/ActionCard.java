package com.hycap.dbt.cards;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hycap.dbt.GameState;

public interface ActionCard extends Card{
    boolean tryPlayCard(GameState gameState, Stage stage);
}
