package com.hycap.dbt.cards;
import com.hycap.dbt.Deck;

public interface ActionCard extends Card{
    boolean tryPlayCard(Deck deck);
}
