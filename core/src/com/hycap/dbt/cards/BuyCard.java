package com.hycap.dbt.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hycap.dbt.GameState;
import com.hycap.dbt.SkinClass;
import com.hycap.dbt.UIManager;
import com.hycap.dbt.tasks.BuyNewCardTask;

import java.util.*;
import java.util.List;

public class BuyCard implements ActionCard, BuyableCard{
    public static Texture texture;
    public static Map<BuyableCard, Float> cardDrawWeights;
    public static Map<BuyableCard, Integer> cardRemainingCount;
    public static int baseShownCardAmount = 3;
    public static int shownCardAmount = baseShownCardAmount;
    public static List<BuyableCard> cardSelection;
    static {
        cardSelection = new ArrayList<>();
        cardDrawWeights = new HashMap<>();
        cardDrawWeights.put(new CoffersCard(), 1.5f);
        cardDrawWeights.put(new Draw2Card(), 2f);
        cardDrawWeights.put(new Remove1Card(), 1f);
        cardDrawWeights.put(new BuyCard(), 1.5f);
        cardDrawWeights.put(new MageCard(), 1.25f);
        cardDrawWeights.put(new PaverCard(), 1.25f);
        cardDrawWeights.put(new TowerCard(), 1.5f);
        cardDrawWeights.put(new WallCard(), 1.75f);
        cardDrawWeights.put(new SniperCard(), 1.5f);
        cardDrawWeights.put(new EarthquakeCard(), 1.5f);
        cardDrawWeights.put(new Recycle2Card(), 1f);
        cardDrawWeights.put(new LibraryCard(), 1.5f);
        cardDrawWeights.put(new SpikesCard(), 1.75f);
        cardDrawWeights.put(new MineCard(), 1.75f);

        cardRemainingCount = new HashMap<>();
        cardRemainingCount.put(new LibraryCard(), 3);
    }

    @Override
    public int getEnergyCost() {
        return 1;
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
                    BuyableCard newCard = (BuyableCard) entry.getKey().duplicate();
                    if (!canBuy(newCard)) {
                        --i;
                        break;
                    }
                    newCards.add(newCard);
                    tempWeights.remove(entry.getKey());
                    break;
                }
            }
        }
        return newCards;
    }

    static void decreaseRemainingCount(Card card) {
        for (BuyableCard key : cardRemainingCount.keySet()) {
            if (key.getClass().equals(card.getClass())) {
                cardRemainingCount.put(key, cardRemainingCount.get(key) - 1);
            }
        }
    }

    boolean canBuy(Card card) {
        for (BuyableCard key : cardRemainingCount.keySet()) {
            if (key.getClass().equals(card.getClass())) {
                int countLeft = cardRemainingCount.get(key);
                if (countLeft <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean tryPlayCard(final GameState gameState, Stage stage) {
        cardSelection = getNewCardSelection(shownCardAmount);
        UIManager.queryTable = new Table();
        UIManager.queryTable.setFillParent(true);

        Label label = new Label("Pick a card to buy", SkinClass.skin);
        UIManager.queryTable.add(label).row();

        final Table cardTable = new Table();
        UIManager.queryTable.setFillParent(true);
        UIManager.queryTable.add(cardTable);

        stage.addActor(UIManager.queryTable);

        gameState.blocked = true;

        for (final BuyableCard card : cardSelection) {
            Label costLabel = new Label(card.getBuyCost() + "G", SkinClass.skin);
            cardTable.add(costLabel);
        }
        cardTable.row();

        gameState.deck.discardCard(this);
        for (final BuyableCard card : cardSelection) {
            TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            cardTable.add(imageButton);
            if (GameState.gameState.gold >= card.getBuyCost()) {
                imageButton.padBottom(30);
            }
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return tryBuyCard(gameState, card);
                }
            });
        }
        TextButton passButton = new TextButton("Pass", SkinClass.skin);
        passButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameState.blocked = false;
                UIManager.queryTable.remove();
                return true;
            }
        });
        UIManager.queryTable.row();
        UIManager.queryTable.add(passButton);
        return true;
    }

    public static boolean tryBuyCard(GameState gameState, BuyableCard card) {
        if (gameState.gold < card.getBuyCost()) {
            return false;
        }
        gameState.gold -= card.getBuyCost();
        gameState.deck.addToHand(card);
        gameState.blocked = false;
        UIManager.queryTable.remove();
        decreaseRemainingCount(card);
        BuyNewCardTask.finished = true;
        gameState.gameStats.incrementCardsBought();
        cardSelection = new ArrayList<>();
        return true;
    }

    @Override
    public String getInfo() {
        return "Spend gold to buy 1 of " + shownCardAmount + " cards.";
    }

    @Override
    public int getBuyCost() {
        return 4;
    }
}
