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
	enum Difficulty {
		EASY,
		NORMAL,
		HARD
	}
	public Difficulty difficulty;
	SpriteBatch batch;

	public Integer selectedIndex;
	List<HasRange> selectedViewTowers;

	public GameScreen(Difficulty difficulty) {
		this.difficulty = difficulty;
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

		InputProcessor buildingProcessor = new InputAdapter() {
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (button == Input.Buttons.RIGHT) {
					selectedIndex = null;
					UIManager.removeSelectedInfo();
					selectedViewTowers = new ArrayList<>();
					return true;
				}
				if (button != Input.Buttons.LEFT) {
					return false;
				}
				Vector3 mousePos = new Vector3(screenX, screenY, 0);
				CameraManager.camera.unproject(mousePos);
				int x = Math.round(mousePos.x);
				int y = Math.round(mousePos.y);
				Building clickedBuilding = GameState.gameState.map.getBuilding(x, y);
				if (clickedBuilding instanceof HasRange) {
					HasRange rangeBuilding = (HasRange) clickedBuilding;
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
				if (GameState.gameState.blocked || GameState.gameState.animating) {
					return false;
				}
				if (selectedIndex != null && selectedIndex >= 0 && selectedIndex < GameState.gameState.deck.getHand().size()) {
					Card card = GameState.gameState.deck.getHandCard(selectedIndex);
					if (GameState.gameState.currentEnergy < card.getEnergyCost()) {
						return false;
					}
					if (card instanceof BuildingCard) {
						BuildingCard buildingCard = (BuildingCard)card;
						Building newBuilding = buildingCard.getBuilding().duplicate();
						newBuilding.setPosition(new Pair<>(x, y));
						if (GameState.gameState.map.placeBuilding(newBuilding, x, y)) {
							newBuilding.onCreate(GameState.gameState);
							GameState.gameState.currentEnergy -= card.getEnergyCost();
							GameState.gameState.deck.discardCard(card);
							selectedIndex = null;
							if (card instanceof ExhaustCard) {
								GameState.gameState.deck.removeCard(card);
							}
						}
					} else if (card instanceof ActionCard) {
						ActionCard actionCard = (ActionCard) card;
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

		InputProcessor shortcutProcessor = new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Input.Keys.SPACE) {
					CameraManager.resetCamera();
					return true;
				}
				if (keycode == Input.Keys.TAB) {
					GameState.gameState.toggleFastForward();
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
				if (GameState.gameState.blocked || GameState.gameState.animating) {
					return false;
				}
				int newSelectedIndex;
				if (keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9) {
					if (keycode == Input.Keys.NUM_0) {
						newSelectedIndex = 9;
					} else {
						newSelectedIndex = keycode - Input.Keys.NUM_1;
					}
					if (selectedIndex != null && newSelectedIndex == selectedIndex) {
						selectedIndex = null;
						UIManager.removeSelectedInfo();
					} else {
						selectedIndex = newSelectedIndex;
						if (GameState.gameState.deck.getHand().size() > selectedIndex) {
							UIManager.setSelectedInfo(GameState.gameState.deck.getHandCard(selectedIndex));
						}
					}
					return true;
				}
				if (keycode == Input.Keys.E) {
					newTurn();
					EndTurnTask.finished = true;
					return true;
				}

				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (button == Input.Buttons.MIDDLE) {
					CameraManager.resetCamera();
					return true;
				}
				return false;
			}
		};

		Gdx.input.setInputProcessor(new InputMultiplexer(UIManager.stage, buildingProcessor, CameraManager.cameraProcessor, shortcutProcessor));
		newTurn();
	}

	void newTurn() {
		GameState.gameState.newTurn();
		selectedIndex = null;
	}

	public void resize (int width, int height) {
		UIManager.stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float deltaT) {
		ScreenUtils.clear(230/255f, 240/255f, 255/255f, 1);

		TaskManager.update();
		CameraManager.update();
		batch.setProjectionMatrix(CameraManager.camera.combined);
		batch.begin();

		int viewRadius = GameState.gameState.map.currentRadius + GameState.gameState.map.extraViewRadius;
		int centrePos = GameState.gameState.map.SIZE / 2;
		TextureManager.draw(batch, TextureManager.grassTexture, centrePos, centrePos,
				0.6f, viewRadius * 2 + 1);
		TextureManager.draw(batch, TextureManager.grassTexture, centrePos, centrePos,
				1f, GameState.gameState.map.currentRadius * 2 + 1);

		for (Pair<Integer> buildingCoords : GameState.gameState.map.getBuildingCoords()) {
			Building building = GameState.gameState.map.getBuilding(buildingCoords.getLeft(), buildingCoords.getRight());
			TextureManager.draw(batch, building.getTexture(), buildingCoords.getLeft(), buildingCoords.getRight());
		}

		Card card;
		if (selectedIndex != null && selectedIndex >= 0 && selectedIndex < GameState.gameState.deck.getHand().size()) {
			card = GameState.gameState.deck.getHandCard(selectedIndex);
			if (!GameState.gameState.blocked && selectedIndex != null && card instanceof BuildingCard) {
				BuildingCard buildingCard = (BuildingCard) GameState.gameState.deck.getHandCard(selectedIndex);
				Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				CameraManager.camera.unproject(mousePos);
				int x = Math.round(mousePos.x);
				int y = Math.round(mousePos.y);
				float alpha = 0;
				if (GameState.gameState.map.canPlaceBuilding(x, y)) {
					alpha = 0.9f;
				} else if (GameState.gameState.map.getBuilding(x, y) == null) {
					alpha = 0.4f;
				}
				TextureManager.draw(batch, buildingCard.getBuilding().getTexture(), x, y, alpha);
				if (buildingCard.getBuilding() instanceof HasRange) {
					HasRange towerBuilding = (HasRange)(buildingCard.getBuilding());
					TextureManager.draw(batch, TextureManager.circleTexture, x, y, 0.5f,
							towerBuilding.getRange() * 2 * TextureManager.circleSizeMult);
				}
			}
		}
		for (EnemyBase enemyBase : GameState.gameState.map.enemyBases) {
			int x = enemyBase.position.getLeft();
			int y = enemyBase.position.getRight();
			if (GameState.gameState.map.isInRadius(x, y)) {
				TextureManager.draw(batch, enemyBase.getTexture(), x, y);
			} else if (GameState.gameState.map.isInViewRadius(x, y)) {
				TextureManager.draw(batch, enemyBase.getTexture(), x, y, 0.6f);
			}
		}
		for (Pair<Integer> coords : GameState.gameState.map.riftCoords) {
			TextureManager.draw(batch, TextureManager.riftTexture, coords.getLeft(), coords.getRight());
		}
		for (Enemy enemy : GameState.gameState.enemies) {
			float scale = 1;
			if (enemy instanceof SetRenderScale) {
				scale = ((SetRenderScale)enemy).genRenderScale();
			}
			TextureManager.draw(batch, enemy.getTexture(), enemy.getX(), enemy.getY(), 1, scale);
		}
		for (HasRange building : selectedViewTowers) {
			int x = building.getPosition().getLeft();
			int y = building.getPosition().getRight();
			TextureManager.draw(batch, TextureManager.circleTexture, x, y, 0.5f,
					building.getRange() * 2 * TextureManager.circleSizeMult);
		}
		for (Projectile projectile : GameState.gameState.projectiles) {
			float x = projectile.positionVector.x;
			float y = projectile.positionVector.y;
			TextureManager.draw(batch, projectile.getTexture(), x, y, 1, projectile.getTextureScale());
		}
		for (EnemyProjectile projectile : GameState.gameState.enemyProjectiles) {
			float x = projectile.positionVector.x;
			float y = projectile.positionVector.y;
			TextureManager.draw(batch, projectile.getTexture(), x, y, 1, projectile.getTextureScale());
		}
		for (int i = 0; i < GameState.gameState.particles.size(); ++i) {
			boolean keep = GameState.gameState.particles.get(i).render(batch, Gdx.graphics.getDeltaTime());
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
