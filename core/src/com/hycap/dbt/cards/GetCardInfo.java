package com.hycap.dbt.cards;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hycap.dbt.SkinClass;

public class GetCardInfo {
    public static String getInfo(Card card) {
        StringBuilder string = new StringBuilder();
        string.append(card.getName()).append("\n");
        string.append(card.getInfo());
        if (card instanceof ExhaustCard) {
            string.append("Remove after playing.");
        }
        return string.toString();
    }

}
