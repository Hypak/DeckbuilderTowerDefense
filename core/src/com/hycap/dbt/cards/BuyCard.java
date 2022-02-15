package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hycap.dbt.GameState;
import com.hycap.dbt.SkinClass;

import java.util.*;
import java.util.List;

public class BuyCard implements ActionCard, BuyableCard{
    public static Texture texture;
    public static Map<BuyableCard, Float> cardDrawWeights;
    static int shownCardAmount = 4;
    static {
        cardDrawWeights = new HashMap<>();
        cardDrawWeights.put(new MineCard(), 2f);
        cardDrawWeights.put(new CoffersCard(), 2f);
        cardDrawWeights.put(new Draw2Card(), 2f);
        cardDrawWeights.put(new Remove1Card(), 1f);
        cardDrawWeights.put(new BuyCard(), 1.5f);
        cardDrawWeights.put(new MageCard(), 1f);
        cardDrawWeights.put(new TowerCard(), 2f);
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }

    @Override
    public String getName() {
        return "Buy Card";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Card duplicate() {
        return new BuyCard();
    }

    private List<BuyableCard> getNewCardSelection(int size) {
        Map<BuyableCard, Float> tempWeights = new HashMap<>(cardDrawWeights);
        List<BuyableCard> newCards = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; ++i) {
            float total = 0;
            for (float v : tempWeights.values()) {
                total += v;
            }
            float r = random.nextFloat() * total;
            for (Map.Entry<BuyableCard, Float> entry : tempWeights.entrySet()) {
                r -= entry.getValue();
                if (r <= 0) {
                    newCards.add((BuyableCard) entry.getKey().duplicate());
                    tempWeights.remove(entry.getKey());
                    break;
                }
            }
        }
        return newCards;
    }

    @Override
    public boolean tryPlayCard(final GameState gameState, Stage stage) {
        List<BuyableCard> cardSelection = getNewCardSelection(shownCardAmount);
        final Table queryTable = new Table();
        queryTable.setFillParent(true);

        Label label = new Label("Pick a card to buy", SkinClass.skin);
        queryTable.add(label).row();

        final Table cardTable = new Table();
        queryTable.setFillParent(true);
        queryTable.add(cardTable);

        stage.addActor(queryTable);

        gameState.blocked = true;

        for (final BuyableCard card : cardSelection) {
            Label costLabel = new Label(card.getBuyCost() + "G", SkinClass.skin);
            cardTable.add(costLabel);
        }
        cardTable.row();

            final Card thisCard = this;
        for (final BuyableCard card : cardSelection) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            cardTable.add(imageButton);
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (gameState.gold < card.getBuyCost()) {
                        return false;
                    }
                    gameState.gold -= card.getBuyCost();
                    gameState.deck.addToDraw(card);
                    gameState.deck.discardCard(thisCard);
                    gameState.blocked = false;
                    queryTable.remove();
                    return true;
                }
            });
        }
        TextButton passButton = new TextButton("Pass", SkinClass.skin);
        passButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameState.deck.discardCard(thisCard);
                gameState.blocked = false;
                queryTable.remove();
                return true;
            }
        });
        queryTable.row();
        queryTable.add(passButton);
        return true;
    }

    @Override
    public int getBuyCost() {
        return 4;
    }
}
