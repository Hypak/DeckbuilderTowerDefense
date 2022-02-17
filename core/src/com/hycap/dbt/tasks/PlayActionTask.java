package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class PlayActionTask implements Task {
    public static boolean finished = false;

    @Override
    public String getTaskText() {
        return "Play an action card from your hand.\nPlaying a card uses up your energy.";
    }

    @Override
    public List<Task> getNextTasks() {
        List<Task> next = new ArrayList<>();
        next.add(new BuyNewCardTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
