package com.hycap.dbt;

import com.hycap.dbt.cards.*;

import java.util.*;

public class Deck {
    private final List<Card> cards;
    private LinkedList<Card> drawPile;
    private List<Card> hand;
    private List<Card> discardPile;
    public int cardsLeftToDiscard;

    Deck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            cards.add(new PathCard());
        }
        cards.add(new MineCard());
        cards.add(new FieldCard());
        cards.add(new FieldCard());
        cards.add(new BuyCard());
        cards.add(new TowerCard());
        cards.add(new Recycle2Card());
        shuffleAll();

        cardsLeftToDiscard = 0;
    }

    public List<Card> getHand() {
        return hand;
    }

    Card getHandCard(final int index) {
        return hand.get(index);
    }

    public List<Card> getCards() {
        return cards;
    }

    LinkedList<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    void discardCardAt(final int index) {
        discardPile.add(hand.get(index));
        hand.remove(index);
    }

    public void addToDraw(final Card card) {
        cards.add(card);
        drawPile.addLast(card);
    }

    public void addToHand(final Card card) {
        cards.add(card);
        hand.add(card);
    }

    void addToHand(final Collection<Card> cards) {
        this.cards.addAll(cards);
        hand.addAll(cards);
    }

    public void removeCard(final Card card) {
        cards.remove(card);
        drawPile.remove(card);
        hand.remove(card);
        discardPile.remove(card);
    }

    public void discardCard(final Card card) {
        if (hand.contains(card)) {
            discardPile.add(card);
            hand.remove(card);
        }
    }

    private void shuffleAll() {
        final List<Card> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        drawPile = new LinkedList<>(shuffledCards);
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    private void shuffleDiscard() {
        Collections.shuffle(discardPile);
        drawPile.addAll(discardPile);
        discardPile = new ArrayList<>();
    }

    void drawNewHand(final int handSize) {
        for (int i = 0; i < hand.size(); ++i) {
            if (hand.get(i) instanceof EtherealCard) {
                cards.remove(hand.get(i));
                hand.remove(i);
                --i;
            }
        }
        discardPile.addAll(hand);
        hand = new ArrayList<>();
        int size = handSize;
        if (size > cards.size()) {
            size = cards.size();
        }
        for (int i = 0; i < size; ++i) {
            Card newCard = drawPile.poll();
            if (newCard == null) {
                shuffleDiscard();
                newCard = drawPile.poll();
            }
            hand.add(newCard);
        }
    }

    public boolean drawNewCard() {
        if (drawPile.isEmpty()) {
            shuffleDiscard();
            if (drawPile.isEmpty()) {
                return false;  // Out of cards
            }
        }
        hand.add(drawPile.poll());
        return true;
    }
}
