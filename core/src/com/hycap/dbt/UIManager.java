package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.tasks.EndTurnTask;
import com.hycap.dbt.tasks.TaskManager;

public class UIManager {
    static Stage stage;
    static Table handTable;
    static TextButton viewAllCards;
    static boolean showingAllCards = false;
    static int maxShowCardWidth = 5;
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

    static Label taskInfoLabel;
    static Table taskInfoTable;

    static TextButton fastForwardButton;
    static Table fastForwardTable;

    public static void setSelectedInfo(Card card) {
        selectedInfo.setText(GetObjectInfo.getInfo(card));
    }

    public static void setSelectedInfo(Building building) {
        selectedInfo.setText(GetObjectInfo.getInfo(building));
    }

    public static void removeSelectedInfo() {
        selectedInfo.setText("");
    }

    public static void hideAllCards() {
        showingAllCards = true;
        toggleShowCards();
    }

    public static void toggleShowCards() {
        showingAllCards = !showingAllCards;
        // GameState.gameState.blocked = showingAllCards;
        cardTable.reset();
        cardTable.align(Align.left);
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
        stage = new Stage(new ExtendViewport(1920, 1080));
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
                if (!GameState.gameState.blocked && !GameState.gameState.animating) {
                    myGdxGame.newTurn();
                    EndTurnTask.finished = true;
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

        taskInfoLabel = new Label("Loading...", SkinClass.skin);
        taskInfoTable = new Table();
        taskInfoTable.add(taskInfoLabel);
        taskInfoTable.setFillParent(true);
        taskInfoTable.align(Align.topRight);
        taskInfoTable.padTop(30);
        taskInfoTable.padRight(30);

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
        stage.addActor(taskInfoTable);
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

        float scale = 1;
        if (GameState.gameState.deck.getHand().size() >= 20) {
            scale = 1 / 2f;
        }
        if (GameState.gameState.deck.getHand().size() >= 12) {
            scale = 3 / 4f;
        }
        int i = 0;
        for (final Card card : GameState.gameState.deck.getHand()) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108 * scale, 192 * scale);
            final ImageButton imageButton = new ImageButton(image, image);
            handTable.add(imageButton);
            if (myGdxGame.selectedIndex != null && myGdxGame.selectedIndex == i) {
                imageButton.padBottom(60);
            } else if (GameState.gameState.currentEnergy >= card.getEnergyCost()) {
                imageButton.padBottom(30);
            }
            imageButton.align(Align.bottom);
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
        int radius = GameState.gameState.map.currentRadius;
        int maxRadius = GameState.gameState.map.WIDTH / 2;
        StringBuilder roundInfoString = new StringBuilder();
        roundInfoString.append("Radius: ").append(Math.min(radius, maxRadius)).append(" / ").append(maxRadius);
        if (radius + GameState.gameState.map.extraViewRadius >= maxRadius) {
            if (radius > maxRadius) {
                roundInfoString.append("\nExtra turns: ").append(radius - maxRadius);
            }
            roundInfoString.append("\nRemaining bases: ").append(GameState.gameState.map.enemyBases.size());
        }
        roundInfo.setText(roundInfoString.toString());

        taskInfoLabel.setText(TaskManager.getAllTaskDescriptions());
    }
}
