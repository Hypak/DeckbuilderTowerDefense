package com.hycap.dbt;

import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.Draw2Card;
import com.hycap.dbt.cards.MineCard;
import com.hycap.dbt.cards.PathCard;

import java.util.*;

public class Deck {
    private final List<Card> cards;
    private LinkedList<Card> drawPile;
    private List<Card> hand;
    private List<Card> discardPile;

    public Deck() {
        this.cards = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            cards.add(new PathCard());
        }
        cards.add(new MineCard());
        cards.add(new MineCard());
        cards.add(new Draw2Card());
        shuffleAll();
    }

    public Deck(List<Card> cards) {
        this.cards = cards;
        shuffleAll();
    }

    public List<Card> getHand() {
        return hand;
    }

    public Card getHandCard(int index) {
        return hand.get(index);
    }

    public void discardCardAt(int index) {
        this.discardPile.add(hand.get(index));
        this.hand.remove(index);
    }

    public void shuffleAll() {
        List<Card> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        this.drawPile = new LinkedList<>(shuffledCards);
        this.hand = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }

    public void shuffleDiscard() {
        Collections.shuffle(discardPile);
        this.drawPile.addAll(this.discardPile);
        this.discardPile = new ArrayList<>();
    }

    public void drawNewHand(int handSize) {
        this.discardPile.addAll(hand);
        this.hand = new ArrayList<>();
        if (handSize > cards.size()) {
            handSize = cards.size();
        }
        for (int i = 0; i < handSize; ++i) {
            Card newCard = this.drawPile.poll();
            if (newCard == null) {
                shuffleDiscard();
                newCard = this.drawPile.poll();
            }
            hand.add(newCard);
        }
    }

    public boolean drawNewCard() {
        if (this.drawPile.size() == 0) {
            shuffleDiscard();
            if (this.drawPile.size() == 0) {
                return false;  // Out of cards
            }
        }
        hand.add(drawPile.poll());
        return true;
    }
}
