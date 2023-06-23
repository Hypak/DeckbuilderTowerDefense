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
import com.hycap.dbt.buildings.CannotBeRemoved;
import com.hycap.dbt.buildings.HasRange;
import com.hycap.dbt.buildings.Upgradable;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.tasks.EndTurnTask;
import com.hycap.dbt.tasks.TaskManager;
import com.hycap.dbt.tasks.UpgradeTask;

public final class UIManager {
    static Stage stage;
    private static TooltipManager tooltipManager;
    private static Table handTable;
    private static TextButton viewAllCards;
    private static boolean showingAllCards = false;
    private static final int maxShowCardWidth = 5;
    private static Table cardTable;
    private static Label goldDisplay;
    private static Label energyDisplay;
    private static Label cardCounts;

    private static Label selectedInfo;
    private static TextButton buildingUpgrade;
    private static TextButton buildingRemove;
    private static int pressesUntilRemove;
    private static final int basePressesUntilRemove = 2;
    private static Table buildingButtonTable;
    private static Table selectedInfoTable;
    private static Building selectedBuilding = null;

    private static Label roundInfo;

    private static Button taskButton;
    private static boolean showingAllCompletedTasks = false;
    private static Label taskInfoLabel;

    static boolean showingMenu = false;
    private static Table menuTable;
    static boolean showingEndGameUI = false;

    public static Table queryTable;

    private UIManager() {
    }

    static void setSelectedInfo(final Card card) {
        removeSelectedInfo();
        selectedInfo.setText(GetObjectInfo.getInfo(card));
    }

    static void setSelectedInfo(final EnemyBase base) {
        removeSelectedInfo();
        selectedInfo.setText(GetObjectInfo.getInfo(base));
    }

    static void setSelectedInfo(final Building building) {
        removeSelectedInfo();
        selectedBuilding = building;
        selectedInfo.setText(GetObjectInfo.getInfo(building));
        buildingButtonTable = new Table();

        selectedInfoTable = new Table();
        selectedInfoTable.add(selectedInfo).row();
        selectedInfoTable.add(buildingButtonTable).row();
        selectedInfoTable.padRight(30);
        selectedInfoTable.setFillParent(true);
        selectedInfoTable.align(Align.right);
        stage.addActor(selectedInfoTable);

        if (building instanceof Upgradable) {
            final Upgradable upgradable = (Upgradable) building;
            buildingUpgrade.remove();
            buildingUpgrade = new TextButton("Upgrade " + upgradable.getUpgradeCost() + " Gold", SkinClass.skin);
            buildingUpgrade.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    if (upgradable.tryUpgrade(GameState.gameState)) {
                        setSelectedInfo(building);
                        UpgradeTask.finished = true;
                    }
                }
            });
            buildingUpgrade.setDisabled(GameState.gameState.animating);

            buildingButtonTable.add(buildingUpgrade).row();
        }
        if (!(building instanceof CannotBeRemoved)) {
            buildingRemove.remove();
            buildingRemove = new TextButton("Remove", SkinClass.skin);
            pressesUntilRemove = basePressesUntilRemove;
            buildingRemove.addListener(new ChangeListener() {
                @Override
                public void changed(final ChangeEvent event, final Actor actor) {
                    --pressesUntilRemove;
                    buildingRemove.setText("Confirm");
                    if (pressesUntilRemove < 1) {
                        GameState.gameState.map.removeBuilding(building);
                        building.onDestroy(GameState.gameState);
                        removeSelectedInfo();
                        if (building instanceof HasRange) {
                            GameScreen.gameScreen.selectedViewTowers.remove((HasRange) building);
                        }
                    }
                }
            });
            buildingRemove.setDisabled(GameState.gameState.animating);
            buildingButtonTable.add(buildingRemove).row();
        }
    }

    public static void updateInfoIfSelected(final Building building) {
        if (building.equals(selectedBuilding)) {
            setSelectedInfo(selectedBuilding);
        }
    }

    static void removeSelectedInfo() {
        selectedInfo.setText("");
        selectedBuilding = null;
        if (buildingButtonTable != null) {
            buildingButtonTable.remove();
        }
    }

    static void startAnimating() {
        buildingUpgrade.setDisabled(true);
    }

    static void endAnimating() {
        buildingUpgrade.setDisabled(false);
    }

    static void hideAllCards() {
        showingAllCards = true;
        toggleShowCards();
    }

    static void toggleShowCards() {
        showingAllCards = !showingAllCards;
        // GameState.gameState.blocked = showingAllCards;
        cardTable.reset();
        cardTable.align(Align.left);
        if (showingAllCards) {
            viewAllCards.setText("Hide deck");
            for (int i = 0; i < GameState.gameState.deck.getCards().size(); ++i) {
                final Card card = GameState.gameState.deck.getCards().get(i);
                final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
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

    static void create(final GameScreen gameScreen) {
        stage = new Stage(new ExtendViewport(1920, 1080));
        tooltipManager = new TooltipManager();
        handTable = new Table();
        cardCounts = new Label("Loading...", SkinClass.skin);
        viewAllCards = new TextButton("View deck", SkinClass.skin);
        goldDisplay = new Label("Loading...", SkinClass.skin);
        energyDisplay = new Label("Loading...", SkinClass.skin);
        final TextButton endTurnButton = new TextButton("End Turn", SkinClass.skin);

        viewAllCards.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                toggleShowCards();
                tooltipManager.instant();
            }
        });
        viewAllCards.addListener(new TextTooltip("V", tooltipManager, SkinClass.skin));

        endTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                if (!GameState.gameState.blocked && !GameState.gameState.animating) {
                    gameScreen.newTurn();
                    EndTurnTask.finished = true;
                }
                tooltipManager.instant();  // I don't know why I need this line lol
            }
        });
        endTurnButton.addListener(new TextTooltip("E", tooltipManager, SkinClass.skin));
        tooltipManager.instant();
        cardTable = new Table();
        cardTable.align(Align.center);
        cardTable.setFillParent(true);

        final Table resourceTable = new Table();
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
        selectedInfoTable.add(selectedInfo).row();
        selectedInfoTable.padRight(30);
        selectedInfoTable.setFillParent(true);
        selectedInfoTable.align(Align.right);

        buildingUpgrade = new TextButton("Upgrade n Gold", SkinClass.skin);
        buildingRemove = new TextButton("Remove", SkinClass.skin);
        buildingButtonTable = new Table(SkinClass.skin);

        roundInfo = new Label("Loading...", SkinClass.skin);
        final Table roundInfoTable = new Table();
        roundInfoTable.add(roundInfo);
        roundInfoTable.setFillParent(true);
        roundInfoTable.align(Align.topLeft);
        roundInfoTable.padTop(30);
        roundInfoTable.padLeft(30);

        taskButton = new TextButton("Tasks", SkinClass.skin);
        taskButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                showingAllCompletedTasks = !showingAllCompletedTasks;
            }
        });
        taskInfoLabel = new Label("Loading...", SkinClass.skin);
        final Table taskInfoTable = new Table();
        taskButton.align(Align.left);
        taskInfoTable.add(taskButton).row();
        taskInfoTable.add(taskInfoLabel);
        taskInfoTable.setFillParent(true);
        taskInfoTable.align(Align.topRight);
        taskInfoTable.padTop(30);
        taskInfoTable.padRight(30);

        final TextButton pauseButton = new TextButton("Pause", SkinClass.skin);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                GameState.gameState.paused = true;
            }
        });
        final TextButton slowSpeedButton = new TextButton(">", SkinClass.skin);
        slowSpeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                GameState.gameState.setRunSpeed(GameState.RunSpeed.SLOW);
                GameState.gameState.paused = false;
            }
        });
        final TextButton mediumSpeedButton = new TextButton(">>", SkinClass.skin);
        mediumSpeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                GameState.gameState.setRunSpeed(GameState.RunSpeed.MEDIUM);
                GameState.gameState.paused = false;
            }
        });
        final TextButton fastSpeedButton = new TextButton(">>>", SkinClass.skin);
        fastSpeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                GameState.gameState.setRunSpeed(GameState.RunSpeed.FAST);
                GameState.gameState.paused = false;
            }
        });
        final TextButton skipButton = new TextButton("-->", SkinClass.skin);
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                GameState.gameState.skipAnimation();
            }
        });
        final Table fastForwardTable = new Table();
        fastForwardTable.add(pauseButton);
        fastForwardTable.add(slowSpeedButton);
        fastForwardTable.add(mediumSpeedButton);
        fastForwardTable.add(fastSpeedButton);
        fastForwardTable.add(skipButton);
        fastForwardTable.padTop(30);
        fastForwardTable.setFillParent(true);
        fastForwardTable.align(Align.top);

        TextButton menuButton = new TextButton("Menu", SkinClass.skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                DBTGame.game.setScreen(new TitleScreen());
            }
        });
        TextButton cancelMenuButton = new TextButton("Back to game", SkinClass.skin);
        cancelMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
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

    static void render(final GameScreen gameScreen) {
        updateHandTable(gameScreen);
        updateDisplays();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private static void updateHandTable(final GameScreen gameScreen) {
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
            final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108 * scale, 192 * scale);
            final ImageButton imageButton = new ImageButton(image, image);
            handTable.add(imageButton);
            if (gameScreen.selectedIndex != null && gameScreen.selectedIndex == i) {
                imageButton.padBottom(60);
            } else if (GameState.gameState.currentEnergy >= card.getEnergyCost()) {
                imageButton.padBottom(30);
            }
            /*
            if (card.getEnergyCost() > GameState.gameState.currentEnergy) {
                // TODO: Make button look disabled
            }
            */
            imageButton.align(Align.bottom);
            final int finalIndex = i;
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
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

    private static void updateDisplays() {
        energyDisplay.setText(GameState.gameState.currentEnergy + " / " + GameState.gameState.baseEnergy + " Energy");
        goldDisplay.setText(GameState.gameState.gold + " / "
                + GameState.gameState.maxGold + " Gold (+" + GameState.gameState.goldPerTurn + " per turn)");
        cardCounts.setText(GameState.gameState.deck.getDrawPile().size() + " Cards in Draw Pile, "
                + GameState.gameState.deck.getCards().size() + " Total");
        final int radius = GameState.gameState.map.currentRadius;
        final int maxRadius = GameState.gameState.map.SIZE / 2;
        final StringBuilder roundInfoString = new StringBuilder();
        roundInfoString.append("Radius: ").append(Math.min(radius, maxRadius)).append(" / ").append(maxRadius);
        if (radius + GameState.gameState.map.extraViewRadius >= maxRadius) {
            if (radius > maxRadius) {
                roundInfoString.append("\nExtra turns: ").append(radius - maxRadius);
            }
            roundInfoString.append("\nRemaining bases: ").append(GameState.gameState.map.enemyBaseManager.enemyBases.size());
        }
        roundInfoString.append("\nNew base at radius: ").append(GameState.gameState.map.enemyBaseManager.getNextBaseRadius());
        final int enemiesNextWave = GameState.gameState.map.enemyBaseManager.getEnemyCountNextWave();
        if (enemiesNextWave > 0) {
            roundInfoString.append("\nEnemies next wave: ").append(enemiesNextWave);
        }
        roundInfo.setText(roundInfoString.toString());

        if (showingAllCompletedTasks) {
            taskInfoLabel.setText(TaskManager.getAllCompletedTaskDescriptions());
        } else {
            taskInfoLabel.setText(TaskManager.getAllActiveTaskDescriptions());
        }
    }

    static void toggleMenuButton() {
        if (showingMenu) {
            showingMenu = false;
            menuTable.remove();
        } else {
            showingMenu = true;
            stage.addActor(menuTable);
        }
    }

    static void showEndGameUI() {
        if (showingEndGameUI) {
            return;
        }
        showingEndGameUI = true;
        final Table table = new Table();
        final Label gameOver = new Label("Game Over", SkinClass.skin);
        table.add(gameOver).row();

        final GameStatistics stats = GameState.gameState.gameStats;
        final Table statsTable = new Table();
        final Label radiusLabel = new Label("Round: " + GameState.gameState.map.currentRadius +
                " / " + GameState.gameState.map.SIZE / 2, SkinClass.skin);
        final Label basesDestroyed = new Label("Bases killed: " + stats.enemyBasesDestroyed +
                " / " + stats.totalEnemyBases, SkinClass.skin);
        final Label buildingsPlaced = new Label("Buildings placed: " + stats.buildingsPlaced, SkinClass.skin);
        final Label cardsBought = new Label("Cards bought: " + stats.cardsBought, SkinClass.skin);
        statsTable.add(radiusLabel).row();
        statsTable.add(basesDestroyed).row();
        statsTable.add(buildingsPlaced).row();
        statsTable.add(cardsBought).row();
        statsTable.padRight(30);

        final Button mainMenuButton = new TextButton("Menu", SkinClass.skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                DBTGame.game.setScreen(new TitleScreen());
            }
        });
        table.add(statsTable, mainMenuButton);
        table.setFillParent(true);
        table.align(Align.center);
        stage.addActor(table);
    }
}
