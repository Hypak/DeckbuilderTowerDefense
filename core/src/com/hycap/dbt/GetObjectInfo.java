package com.hycap.dbt;

import com.hycap.dbt.buildings.Building;
import com.hycap.dbt.cards.Card;
import com.hycap.dbt.cards.ExhaustCard;

public class GetObjectInfo {
    public static String getInfo(Card card) {
        StringBuilder string = new StringBuilder();
        string.append(card.getName()).append("\n");
        string.append(card.getInfo());
        if (card instanceof ExhaustCard) {
            string.append("\nRemove after playing.");
        }
        return string.toString();
    }

    public static String getInfo(Building building) {
        StringBuilder string = new StringBuilder();
        string.append(building.getName()).append("\n");
        string.append(building.getInfo()).append("\n");
        string.append(building.getStats());
        return string.toString();
    }

}
