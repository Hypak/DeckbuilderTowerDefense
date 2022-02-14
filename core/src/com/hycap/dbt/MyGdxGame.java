package com.hycap.dbt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hycap.dbt.buildings.*;
import com.hycap.dbt.cards.*;

public class MyGdxGame extends ApplicationAdapter {
	Deck deck;
	Map map;
	int baseHandSize = 5;
	int baseEnergy = 3;
	int currentEnergy;

	SpriteBatch batch;
	OrthographicCamera camera;
	float panSpeed = 15;

	Stage stage;
	Table handTable;
	Skin skin;
	Label energyDisplay;
	TextButton endTurnButton;
	Table energyEndTable;

	Table uiTable;

	Integer selectedIndex;

	private void setTextures() {
		CentralBuilding.texture = new Texture("CentralBuilding.png");
		PathBuilding.texture = new Texture("PathBuilding.png");
		PathCard.texture = new Texture("PathCard.png");
		MineBuilding.texture = new Texture("MineBuilding.png");
		MineCard.texture = new Texture("MineCard.png");
		CoffersBuilding.texture = new Texture("CoffersBuilding.png");
		CoffersCard.texture = new Texture("CoffersCard.png");

		Draw2Card.texture = new Texture("Draw2Card.png");
		Remove1Card.texture = new Texture("Remove1Card.png");
	}

	@Override
	public void create () {
		deck = new Deck();
		deck.drawNewHand(baseHandSize);
		map = new Map();

		setTextures();

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 20,
				20f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		skin = new Skin(Gdx.files.internal("gdx-skins-master/skin-composer/skin/skin-composer-ui.json"));
		skin.getFont("font").getData().setScale(2, 2);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		handTable = new Table();
		updateHandTable();
		energyDisplay = new Label("Loading...", skin);
		// energyDisplay.setFillParent(true);
		endTurnButton = new TextButton("End Turn", skin);
		endTurnButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				newTurn();
			}
		});
		energyEndTable = new Table();
		energyEndTable.add(energyDisplay).row();
		energyEndTable.add(endTurnButton).row();
		energyEndTable.padBottom(30);
		energyEndTable.padRight(30);
		energyEndTable.setFillParent(true);
		energyEndTable.align(Align.bottomRight);


		stage.addActor(handTable);
		stage.addActor(energyEndTable);

		InputProcessor cameraProcessor = new InputAdapter() {
			@Override
			public boolean scrolled(float amountX, float amountY) {
				double zoomSpeed = 1.1f;
				float zoomMult = (float)Math.pow(zoomSpeed, amountY);
				camera.zoom *= zoomMult;
				Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(mousePos);
				Vector3 moveVec = new Vector3(camera.position).sub(mousePos);
				moveVec.scl(zoomMult - 1);
				camera.translate(moveVec);
				return true;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (button != 0) {
					return false;
				}
				if (selectedIndex != null && selectedIndex >= 0 && selectedIndex < deck.getHand().size()) {
					Card card = deck.getHandCard(selectedIndex);
					if (currentEnergy < card.getEnergyCost()) {
						return false;
					}
					if (card instanceof BuildingCard) {
						Vector3 mousePos = new Vector3(screenX, screenY, 0);
						camera.unproject(mousePos);
						int x = Math.round(mousePos.x);
						int y = Math.round(mousePos.y);
						if (selectedIndex >= deck.getHand().size()) {
							return false;
						}

						BuildingCard buildingCard = (BuildingCard)card;
						if (map.placeBuilding(buildingCard.getBuilding(), x, y)) {
							discardCardAt(selectedIndex);
							updateEnergyDisplay();
						}
					} else if (card instanceof ActionCard) {
						ActionCard actionCard = (ActionCard) card;
						if (actionCard.tryPlayCard(deck)) {
							discardCardAt(selectedIndex);
							updateEnergyDisplay();
						}
					}
				}
				return true;
			}
		};

		InputProcessor shortcutProcessor = new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
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

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, cameraProcessor, shortcutProcessor));
		newTurn();
	}

	void discardCardAt(int index) {
		currentEnergy -= deck.getHandCard(index).getEnergyCost();
		deck.discardCardAt(index);
		selectedIndex = null;
		updateHandTable();
	}

	void newTurn() {
		deck.drawNewHand(baseHandSize);
		updateHandTable();
		currentEnergy = baseEnergy;
		updateEnergyDisplay();
	}

	public void updateHandTable() {
		handTable.reset();
		handTable.setFillParent(true);
		handTable.bottom();

		int i = 0;
		for (Card card : deck.getHand()) {
			TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
			image.setMinSize(108, 192);
			final ImageButton imageButton = new ImageButton(image, image);
			handTable.add(imageButton);
			final int finalIndex = i;
			imageButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					selectedIndex = finalIndex;
					return true;
				}
			});
			++i;
		}
	}

	public void updateEnergyDisplay() {
		energyDisplay.setText(currentEnergy + " / " + baseEnergy + " Energy");
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		float move = Gdx.graphics.getDeltaTime() * camera.zoom * panSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.position.y += move;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.position.y -= move;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.position.x += move;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.position.x -= move;
		}

		ScreenUtils.clear(230/255f, 240/255f, 255/255f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int x = 0; x < map.WIDTH; ++x) {
			for (int y = 0; y < map.HEIGHT; ++y) {
				Building building = map.getBuilding(x, y);
				if (building != null) {
					Sprite sprite = new Sprite(building.getTexture());
					sprite.setScale(1 / 32f);
					sprite.setPosition(x - 16, y - 16);
					sprite.draw(batch);
				}
			}
		}
		batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
