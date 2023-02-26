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

@SuppressWarnings("StringConcatenationMissingWhitespace")
public class BuyCard implements ActionCard, BuyableCard{
    public static Texture texture;
    private static final Map<BuyableCard, Float> cardDrawWeights;
    private static final Map<BuyableCard, Integer> cardRemainingCount;
    public static final int baseShownCardAmount = 3;
    public static int shownCardAmount = baseShownCardAmount;
    public static List<BuyableCard> cardSelection;
    static {
        cardSelection = new ArrayList<>();
        cardDrawWeights = new HashMap<>(14);
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

        cardRemainingCount = new HashMap<>(1);
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

    private List<BuyableCard> getNewCardSelection(final int size) {
        final Map<BuyableCard, Float> tempWeights = new HashMap<>(cardDrawWeights);
        final List<BuyableCard> newCards = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < size; ++i) {
            float total = 0;
            for (final float v : tempWeights.values()) {
                total += v;
            }
            float r = random.nextFloat() * total;
            for (final Map.Entry<BuyableCard, Float> entry : tempWeights.entrySet()) {
                r -= entry.getValue();
                if (r <= 0) {
                    final BuyableCard newCard = (BuyableCard) entry.getKey().duplicate();
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

    private static void decreaseRemainingCount(final Card card) {
        for (final BuyableCard key : cardRemainingCount.keySet()) {
            if (key.getClass().equals(card.getClass())) {
                cardRemainingCount.put(key, cardRemainingCount.get(key) - 1);
            }
        }
    }

    private boolean canBuy(final Card card) {
        for (final BuyableCard key : cardRemainingCount.keySet()) {
            if (key.getClass().equals(card.getClass())) {
                final int countLeft = cardRemainingCount.get(key);
                if (countLeft <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean tryPlayCard(final GameState gameState, final Stage stage) {
        cardSelection = getNewCardSelection(shownCardAmount);
        UIManager.queryTable = new Table();
        UIManager.queryTable.setFillParent(true);

        final Label label = new Label("Pick a card to buy", SkinClass.skin);
        UIManager.queryTable.add(label).row();

        final Table cardTable = new Table();
        UIManager.queryTable.setFillParent(true);
        UIManager.queryTable.add(cardTable);

        stage.addActor(UIManager.queryTable);

        gameState.blocked = true;

        for (final BuyableCard card : cardSelection) {
            final Label costLabel = new Label(card.getBuyCost() + "G", SkinClass.skin);
            cardTable.add(costLabel);
        }
        cardTable.row();

        gameState.deck.discardCard(this);
        for (final BuyableCard card : cardSelection) {
            final TextureRegionDrawable image = new TextureRegionDrawable(new TextureRegion(card.getTexture()));
            image.setMinSize(108, 192);
            final ImageButton imageButton = new ImageButton(image, image);
            cardTable.add(imageButton);
            if (GameState.gameState.gold >= card.getBuyCost()) {
                imageButton.padBottom(30);
            }
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                    return tryBuyCard(gameState, card);
                }
            });
        }
        final TextButton passButton = new TextButton("Pass", SkinClass.skin);
        passButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                gameState.blocked = false;
                UIManager.queryTable.remove();
                return true;
            }
        });
        UIManager.queryTable.row();
        UIManager.queryTable.add(passButton);
        return true;
    }

    public static boolean tryBuyCard(final GameState gameState, final BuyableCard card) {
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
