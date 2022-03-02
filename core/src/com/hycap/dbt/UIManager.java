package com.hycap.dbt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

    static boolean showingMenu = false;
    static TextButton menuButton;
    static TextButton cancelMenuButton;
    static Table menuTable;
    static boolean showingEndGameUI = false;

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

    public static void create(final GameScreen gameScreen) {
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
                    gameScreen.newTurn();
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

        menuButton = new TextButton("Menu", SkinClass.skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DBTGame.game.setScreen(new TitleScreen());
            }
        });
        cancelMenuButton = new TextButton("Back to game", SkinClass.skin);
        cancelMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMenuButton();
            }
        });
        menuTable = new Table();
        menuTable.add(menuButton).row();
        menuTable.add(cancelMenuButton);
        menuTable.setFillParent(true);
        menuTable.align(Align.center);

        stage.addActor(cardTable);
        stage.addActor(handTable);
        stage.addActor(resourceTable);
        stage.addActor(selectedInfoTable);
        stage.addActor(taskInfoTable);
        stage.addActor(roundInfoTable);
        stage.addActor(fastForwardTable);
    }

    public static void render(GameScreen gameScreen) {
        updateHandTable(gameScreen);
        updateDisplays();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public static void updateHandTable(final GameScreen gameScreen) {
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
            if (gameScreen.selectedIndex != null && gameScreen.selectedIndex == i) {
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
                        gameScreen.selectedIndex = finalIndex;
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
        int maxRadius = GameState.gameState.map.SIZE / 2;
        StringBuilder roundInfoString = new StringBuilder();
        roundInfoString.append("Radius: ").append(Math.min(radius, maxRadius)).append(" / ").append(maxRadius);
        if (radius + GameState.gameState.map.extraViewRadius >= maxRadius) {
            if (radius > maxRadius) {
                roundInfoString.append("\nExtra turns: ").append(radius - maxRadius);
            }
            roundInfoString.append("\nRemaining bases: ").append(GameState.gameState.map.enemyBases.size());
        }
        roundInfoString.append("\nNew base at radius: ").append(GameState.gameState.map.getNextBaseRadius());
        int enemiesNextWave = GameState.gameState.map.getEnemyCountNextWave();
        if (enemiesNextWave > 0) {
            roundInfoString.append("\nEnemies next wave: ").append(enemiesNextWave);
        }
        roundInfo.setText(roundInfoString.toString());

        taskInfoLabel.setText(TaskManager.getAllTaskDescriptions());
    }

    public static void toggleMenuButton() {
        if (showingMenu) {
            showingMenu = false;
            menuTable.remove();
        } else {
            showingMenu = true;
            stage.addActor(menuTable);
        }
    }

    public static void showEndGameUI() {
        if (showingEndGameUI) {
            return;
        }
        showingEndGameUI = true;
        Table table = new Table();
        Label gameOver = new Label("Game Over", SkinClass.skin);
        table.add(gameOver).row();

        GameStatistics stats = GameState.gameState.gameStats;
        Table statsTable = new Table();
        Label radiusLabel = new Label("Round: " + GameState.gameState.map.currentRadius +
                " / " + GameState.gameState.map.SIZE / 2, SkinClass.skin);
        Label basesDestroyed = new Label("Bases killed: " + stats.enemyBasesDestroyed +
                " / " + stats.totalEnemyBases, SkinClass.skin);
        Label buildingsPlaced = new Label("Buildings placed: " + stats.buildingsPlaced, SkinClass.skin);
        Label cardsBought = new Label("Cards bought: " + stats.cardsBought, SkinClass.skin);
        statsTable.add(radiusLabel).row();
        statsTable.add(basesDestroyed).row();
        statsTable.add(buildingsPlaced).row();
        statsTable.add(cardsBought).row();
        statsTable.padRight(30);

        Button mainMenuButton = new TextButton("Menu", SkinClass.skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DBTGame.game.setScreen(new TitleScreen());
            }
        });
        table.add(statsTable, mainMenuButton);
        table.setFillParent(true);
        table.align(Align.center);
        stage.addActor(table);
    }
}
