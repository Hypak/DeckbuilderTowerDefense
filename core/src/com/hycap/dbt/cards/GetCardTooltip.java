package com.hycap.dbt.cards;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.hycap.dbt.SkinClass;

public class GetCardTooltip {
    public static String getTooltipString(Card card) {
        StringBuilder string = new StringBuilder();
        string.append(card.getName()).append("\n");
        string.append(card.getInfo());
        if (card instanceof ExhaustCard) {
            string.append("Remove after playing.");
        }
        return string.toString();
    }

    public static Label getTooltip(Card card) {
        String contents = getTooltipString(card);
        return new Label(contents, SkinClass.skin);
    }
}
