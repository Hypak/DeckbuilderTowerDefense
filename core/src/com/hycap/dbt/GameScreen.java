package com.hycap.dbt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.cards.*;
import com.hycap.dbt.enemies.*;
import com.hycap.dbt.projectiles.EnemyProjectile;
import com.hycap.dbt.projectiles.Projectile;
import com.hycap.dbt.tasks.ClickBuildingTask;
import com.hycap.dbt.tasks.EndTurnTask;
import com.hycap.dbt.tasks.PlayActionTask;
import com.hycap.dbt.tasks.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends ScreenAdapter {
	public static GameScreen gameScreen;
	enum Difficulty {
		CREATIVE,
		EASY,
		NORMAL,
		HARD
	}
	private final Difficulty difficulty;
	private SpriteBatch batch;

	Integer selectedIndex = null;
	List<HasRange> selectedViewTowers = null;

	GameScreen(final Difficulty difficulty) {
		this.difficulty = difficulty;
		gameScreen = this;
	}

	@Override
	public void show() {
		GameState.gameState = new GameState(difficulty);
		CameraManager.create();
		UIManager.create(this);
		UIManager.showingMenu = false;

		TextureManager.setTextures();

		batch = new SpriteBatch();

		selectedViewTowers = new ArrayList<>();
		UIManager.showingEndGameUI = false;

		final InputProcessor buildingProcessor = new InputAdapter() {
			@Override
			public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
				if (button == Input.Buttons.RIGHT) {
					selectedIndex = null;
					UIManager.removeSelectedInfo();
					selectedViewTowers = new ArrayList<>();
					return true;
				}
				if (button != Input.Buttons.LEFT) {
					return false;
				}
				final Vector3 mousePos = new Vector3(screenX, screenY, 0);
				CameraManager.camera.unproject(mousePos);
				final int x = Math.round(mousePos.x);
				final int y = Math.round(mousePos.y);
				final Building clickedBuilding = GameState.gameState.map.getBuilding(x, y);
				if (clickedBuilding instanceof HasRange) {
					final HasRange rangeBuilding = (HasRange) clickedBuilding;
					if (selectedViewTowers.contains(rangeBuilding)) {
						selectedViewTowers.remove(rangeBuilding);
					} else {
						selectedViewTowers.add(rangeBuilding);
					}
				}
				if (clickedBuilding != null) {
					UIManager.setSelectedInfo(clickedBuilding);
					ClickBuildingTask.finished = true;
				}
				final EnemyBase base = GameState.gameState.map.enemyBaseManager.getEnemyBaseAt(x, y);
				if (base != null) {
					UIManager.setSelectedInfo(base);
				}
				if (GameState.gameState.blocked || GameState.gameState.animating) {
					return false;
				}
				if (selectedIndex != null && selectedIndex >= 0 && selectedIndex < GameState.gameState.deck.getHand().size()) {
					final Card card = GameState.gameState.deck.getHandCard(selectedIndex);
					if (GameState.gameState.currentEnergy < card.getEnergyCost()) {
						return false;
					}
					if (card instanceof BuildingCard) {
						final BuildingCard buildingCard = (BuildingCard)card;
						final Building newBuilding = buildingCard.getBuilding().duplicate();
						newBuilding.setPosition(new Pair<>(x, y));
						if (GameState.gameState.map.placeBuilding(newBuilding, x, y)) {
							final boolean onRift = GameState.gameState.map.riftCoords.contains(newBuilding.getPosition());
							newBuilding.onCreate(GameState.gameState, onRift);
							GameState.gameState.currentEnergy -= card.getEnergyCost();
							GameState.gameState.deck.discardCard(card);
							selectedIndex = null;
							if (card instanceof ExhaustCard) {
								GameState.gameState.deck.removeCard(card);
							}
						}
					} else if (card instanceof ActionCard) {
						final ActionCard actionCard = (ActionCard) card;
						if (actionCard.tryPlayCard(GameState.gameState, UIManager.stage)) {
							GameState.gameState.currentEnergy -= card.getEnergyCost();
							selectedIndex = null;
							if (card instanceof ExhaustCard) {
								GameState.gameState.deck.removeCard(card);
							}
							PlayActionTask.finished = true;
						}
					}
				}
				return true;
			}
		};

		final InputProcessor shortcutProcessor = new InputAdapter() {
			@Override
			public boolean keyUp(final int keycode) {
				if (keycode == Input.Keys.SPACE) {
					CameraManager.resetCamera();
					return true;
				}

				if (keycode == Input.Keys.TAB) {
					GameState.gameState.nextRunSpeed();
					return true;
				}
				if (keycode == Input.Keys.ESCAPE) {
					UIManager.hideAllCards();
					UIManager.toggleMenuButton();
					selectedViewTowers = new ArrayList<>();
					selectedIndex = null;
				}
				if (keycode == Input.Keys.V) {
					UIManager.toggleShowCards();
				}
				if (keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9) {
					pressNum(keycode);
				}
				if (GameState.gameState.blocked || GameState.gameState.animating) {
					return false;
				}
				if (keycode == Input.Keys.E) {
					newTurn();
					EndTurnTask.finished = true;
					return true;
				}

				return false;
			}

			@Override
			public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
				if (button == Input.Buttons.MIDDLE) {
					CameraManager.resetCamera();
					return true;
				}
				if (button == Input.Buttons.BACK) {
					if (selectedIndex == null) {
						selectedIndex = GameState.gameState.deck.getHand().size() - 1;
						return true;
					}
					--selectedIndex;
					if (selectedIndex < 0) {
						selectedIndex = null;
					}
					return true;
				}
				if (button == Input.Buttons.FORWARD) {
					if (selectedIndex == null) {
						selectedIndex = 0;
						return true;
					}
					++selectedIndex;
					if (selectedIndex >= GameState.gameState.deck.getHand().size()) {
						selectedIndex = null;
					}
					return true;
				}
				return false;
			}
		};

		Gdx.input.setInputProcessor(new InputMultiplexer(UIManager.stage, buildingProcessor, CameraManager.cameraProcessor, shortcutProcessor));
		newTurn();
	}

	private void pressNum(final int keycode) {
		final int newSelectedIndex;
		if (keycode == Input.Keys.NUM_0) {
			newSelectedIndex = 9;
		} else {
			newSelectedIndex = keycode - Input.Keys.NUM_1;
		}
		final Deck deck = GameState.gameState.deck;
		if (GameState.gameState.blocked) {
			if (deck.cardsLeftToDiscard > 0) {
				deck.discardCardAt(newSelectedIndex);
				--deck.cardsLeftToDiscard;
				if (deck.cardsLeftToDiscard <= 0) {
					UIManager.queryTable.remove();
					GameState.gameState.blocked = false;
				} else {
					Recycle2Card.createQueryTable(GameState.gameState, UIManager.stage);
				}
			} else if (!BuyCard.cardSelection.isEmpty() && newSelectedIndex < BuyCard.cardSelection.size()) {
				BuyCard.tryBuyCard(GameState.gameState, BuyCard.cardSelection.get(newSelectedIndex));
			}
			return;
		}

		if (selectedIndex != null && newSelectedIndex == selectedIndex) {
			selectedIndex = null;
			UIManager.removeSelectedInfo();
		} else {
			selectedIndex = newSelectedIndex;
			if (selectedIndex < GameState.gameState.deck.getHand().size()) {
				UIManager.setSelectedInfo(GameState.gameState.deck.getHandCard(selectedIndex));
			}
		}
	}

	void newTurn() {
		GameState.gameState.newTurn();
		GameState.gameState.paused = false;
		selectedIndex = null;
	}

	public void resize (final int width, final int height) {
		UIManager.stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(final float deltaT) {
		ScreenUtils.clear(230/255f, 240/255f, 255/255f, 1);

		TaskManager.update();
		CameraManager.update();
		batch.setProjectionMatrix(CameraManager.camera.combined);
		batch.begin();

		final int viewRadius = GameState.gameState.map.currentRadius + GameState.gameState.map.extraViewRadius;
		final int centrePos = GameState.gameState.map.SIZE / 2;
		TextureManager.draw(batch, TextureManager.grassTexture, centrePos, centrePos,
				0.6f, viewRadius * 2 + 1);
		TextureManager.draw(batch, TextureManager.grassTexture, centrePos, centrePos,
				1f, GameState.gameState.map.currentRadius * 2 + 1);

		for (final Building building : GameState.gameState.map.getBuildingList()) {
			TextureManager.draw(batch, building.getTexture(),
					building.getPosition().getLeft(), building.getPosition().getRight());
		}

		final Card card;
		if (selectedIndex != null && selectedIndex >= 0 && selectedIndex < GameState.gameState.deck.getHand().size()) {
			card = GameState.gameState.deck.getHandCard(selectedIndex);
			if (!GameState.gameState.blocked && selectedIndex != null && card instanceof BuildingCard) {
				final BuildingCard buildingCard = (BuildingCard) GameState.gameState.deck.getHandCard(selectedIndex);
				final Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				CameraManager.camera.unproject(mousePos);
				final int x = Math.round(mousePos.x);
				final int y = Math.round(mousePos.y);
				float alpha = 0;
				if (GameState.gameState.map.canPlaceBuilding(x, y)) {
					alpha = 0.9f;
				} else if (GameState.gameState.map.getBuilding(x, y) == null) {
					alpha = 0.4f;
				}
				TextureManager.draw(batch, buildingCard.getBuilding().getTexture(), x, y, alpha);
				if (buildingCard.getBuilding() instanceof HasRange) {
					final HasRange towerBuilding = (HasRange)(buildingCard.getBuilding());
					float range = towerBuilding.getRange();
					if (GameState.gameState.map.riftCoords.contains(new Pair<>(x, y))) {
						range *= AbstractTowerBuilding.riftRangeMult;
					}
					TextureManager.draw(batch, TextureManager.circleTexture, x, y, 0.5f,
							range * 2 * TextureManager.circleSizeMult);
				}
			}
		}
		for (final EnemyBase enemyBase : GameState.gameState.map.enemyBaseManager.enemyBases) {
			final int x = enemyBase.position.getLeft();
			final int y = enemyBase.position.getRight();
			if (GameState.gameState.map.isInRadius(x, y)) {
				TextureManager.draw(batch, enemyBase.getTexture(), x, y);
			} else if (GameState.gameState.map.isInViewRadius(x, y)) {
				TextureManager.draw(batch, enemyBase.getTexture(), x, y, 0.6f);
			}
		}
		for (final Pair<Integer> coords : GameState.gameState.map.riftCoords) {
			TextureManager.draw(batch, TextureManager.riftTexture, coords.getLeft(), coords.getRight());
		}
		for (final Enemy enemy : GameState.gameState.enemies) {
			float scale = 1;
			if (enemy instanceof SetRenderScale) {
				scale = ((SetRenderScale)enemy).getRenderScale();
			}
			TextureManager.draw(batch, enemy.getTexture(), enemy.getX(), enemy.getY(), 1, scale);
		}
		for (final HasRange building : selectedViewTowers) {
			final int x = building.getPosition().getLeft();
			final int y = building.getPosition().getRight();
			TextureManager.draw(batch, TextureManager.circleTexture, x, y, 0.5f,
					building.getRange() * 2 * TextureManager.circleSizeMult);
		}
		for (final Projectile projectile : GameState.gameState.projectiles) {
			final float x = projectile.positionVector.x;
			final float y = projectile.positionVector.y;
			TextureManager.draw(batch, projectile.getTexture(), x, y, 1, projectile.getTextureScale());
		}
		for (final EnemyProjectile projectile : GameState.gameState.enemyProjectiles) {
			final float x = projectile.positionVector.x;
			final float y = projectile.positionVector.y;
			TextureManager.draw(batch, projectile.getTexture(), x, y, 1, projectile.getTextureScale());
		}
		for (int i = 0; i < GameState.gameState.particles.size(); ++i) {
			final boolean keep = GameState.gameState.particles.get(i).render(batch, Gdx.graphics.getDeltaTime());
			if (!keep) {
				GameState.gameState.particles.remove(i);
				--i;
			}
		}
		batch.end();
		UIManager.render(this);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
