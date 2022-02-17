package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.cards.Card;

public class UIManager {
    static Stage stage;
    static Table handTable;
    static TextButton viewAllCards;
    static boolean showingAllCards = false;
    static int maxShowCardWidth = 10;
    static Table cardTable;
    static Label goldDisplay;
    static Label energyDisplay;
    static TextButton endTurnButton;
    static Label cardCounts;
    static Table resourceTable;

    static Label selectedInfo;
    static Table selectedInfoTable;
    static Label roundInfo;
    static Table roundInfoTable;

    static TextButton fastForwardButton;
    static Table fastForwardTable;

    public static void setSelectedInfo(Card card) {
        selectedInfo.setText(GetObjectInfo.getInfo(card));
    }

    public static void setSelectedInfo(Building building) {
        selectedInfo.setText(GetObjectInfo.getInfo(building));
    }

    public static void hideAllCards() {
        showingAllCards = true;
        toggleShowCards();
    }

    public static void toggleShowCards() {
        showingAllCards = !showingAllCards;
        GameState.gameState.blocked = showingAllCards;
        cardTable.reset();
        if (showingAllCards) {
            viewAllCards.setText("Hide deck");
            for (int i = 0; i < GameState.gameState.deck.getCards().size(); ++i) {
                Card card = GameState.gameState.deck.getCards().get(i);
                TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
                image.setMinSize(108, 192);
                final ImageButton imageButton = new ImageButton(image, image);
                cardTable.add(imageButton);
                if (i % maxShowCardWidth == maxShowCardWidth - 1) {
                    cardTable.row();
                }
            }
        } else {
            viewAllCards.setText("View deck");
        }
    }

    public static void create(final MyGdxGame myGdxGame) {
        stage = new Stage();
        handTable = new Table();
        cardCounts = new Label("Loading...", SkinClass.skin);
        viewAllCards = new TextButton("View deck", SkinClass.skin);
        goldDisplay = new Label("Loading...", SkinClass.skin);
        energyDisplay = new Label("Loading...", SkinClass.skin);
        endTurnButton = new TextButton("End Turn", SkinClass.skin);

        viewAllCards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleShowCards();
            }
        });
        endTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!GameState.gameState.blocked) {
                    myGdxGame.newTurn();
                }
            }
        });

        cardTable = new Table();
        cardTable.align(Align.center);
        cardTable.setFillParent(true);

        resourceTable = new Table();
        resourceTable.add(viewAllCards).row();
        resourceTable.add(cardCounts).row();
        resourceTable.add(goldDisplay).row();
        resourceTable.add(energyDisplay).row();
        resourceTable.add(endTurnButton).row();
        resourceTable.padBottom(30);
        resourceTable.padRight(30);
        resourceTable.setFillParent(true);
        resourceTable.align(Align.bottomRight);

        selectedInfo = new Label("", SkinClass.skin);
        selectedInfo.setAlignment(Align.right);
        selectedInfo.setWrap(true);
        selectedInfoTable = new Table();
        selectedInfoTable.add(selectedInfo);
        selectedInfoTable.setFillParent(true);
        selectedInfoTable.align(Align.right);
        selectedInfoTable.padRight(30);

        roundInfo = new Label("Loading...", SkinClass.skin);
        roundInfoTable = new Table();
        roundInfoTable.add(roundInfo);
        roundInfoTable.setFillParent(true);
        roundInfoTable.align(Align.topLeft);
        roundInfoTable.padTop(30);
        roundInfoTable.padLeft(30);

        fastForwardButton = new TextButton(">>>", SkinClass.skin);
        fastForwardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameState.gameState.toggleFastForward();
            }
        });
        fastForwardTable = new Table();
        fastForwardTable.add(fastForwardButton);
        fastForwardTable.padTop(30);
        fastForwardTable.setFillParent(true);
        fastForwardTable.align(Align.top);

        stage.addActor(cardTable);
        stage.addActor(handTable);
        stage.addActor(resourceTable);
        stage.addActor(selectedInfoTable);
        stage.addActor(roundInfoTable);
        stage.addActor(fastForwardTable);
    }

    public static void render(MyGdxGame myGdxGame) {
        updateHandTable(myGdxGame);
        updateDisplays();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public static void updateHandTable(final MyGdxGame myGdxGame) {
        handTable.reset();
        handTable.setFillParent(true);
        handTable.align(Align.bottom);

        int i = 0;
        for (final Card card : GameState.gameState.deck.getHand()) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            handTable.add(imageButton);
            if (GameState.gameState.currentEnergy >= card.getEnergyCost()) {
                imageButton.padBottom(30);
            }
            final int finalIndex = i;
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        myGdxGame.selectedIndex = finalIndex;
                        setSelectedInfo(card);
                        return true;
                    }
                    return false;
                }
            });
            ++i;
        }
    }

    public static void updateDisplays() {
        energyDisplay.setText(GameState.gameState.currentEnergy + " / " + GameState.gameState.baseEnergy + " Energy");
        goldDisplay.setText(GameState.gameState.gold + " / "
                + GameState.gameState.maxGold + " Gold (+" + GameState.gameState.goldPerTurn + " per turn)");
        cardCounts.setText(GameState.gameState.deck.getDrawPile().size() + " Cards in Draw Pile, "
                + GameState.gameState.deck.getCards().size() + " Total");
        roundInfo.setText("Radius: " + GameState.gameState.map.currentRadius + " / " + GameState.gameState.map.WIDTH / 2);
    }
}
