package com.hycap.dbt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.cards.*;
import com.hycap.dbt.enemies.*;

import static com.badlogic.gdx.utils.Align.topRight;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;

	Integer selectedIndex;

	@Override
	public void create () {
		SkinClass.skin = new Skin(Gdx.files.internal("gdx-skins-master/tubular/skin/tubular-ui.json"));
		SkinClass.skin.getFont("font").getData().setScale(2, 2);

		GameState.gameState = new GameState();
		CameraManager.create();
		UIManager.create(this);

		TextureManager.setTextures();

		batch = new SpriteBatch();

		InputProcessor buildingProcessor = new InputAdapter() {
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (button != 0) {
					return false;
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
						Vector3 mousePos = new Vector3(screenX, screenY, 0);
						CameraManager.camera.unproject(mousePos);
						int x = Math.round(mousePos.x);
						int y = Math.round(mousePos.y);

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
				if (GameState.gameState.blocked || GameState.gameState.animating) {
					return false;
				}
				if (keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9) {
					if (keycode == Input.Keys.NUM_0) {
						selectedIndex = 9;
					} else {
						selectedIndex = keycode - Input.Keys.NUM_1;
					}
					return true;
				} else if (keycode == Input.Keys.E) {
					newTurn();
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
	public void render () {
		ScreenUtils.clear(230/255f, 240/255f, 255/255f, 1);

		CameraManager.update();
		batch.setProjectionMatrix(CameraManager.camera.combined);
		batch.begin();

		for (int x = 0; x < GameState.gameState.map.WIDTH; ++x) {
			for (int y = 0; y < GameState.gameState.map.HEIGHT; ++y) {
				Building building = GameState.gameState.map.getBuilding(x, y);
				if (building == null) {
					if (!GameState.gameState.map.isEnemyBaseAt(x, y)) {
						if (GameState.gameState.map.isInRadius(x, y)) {
							TextureManager.draw(batch, TextureManager.grassTexture, x, y);
						} else if (GameState.gameState.map.isInViewRadius(x, y)) {
							TextureManager.draw(batch, TextureManager.grassTexture, x, y, 0.6f);
						}
					}
				} else {
					TextureManager.draw(batch, building.getTexture(), x, y);
				}
			}
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
