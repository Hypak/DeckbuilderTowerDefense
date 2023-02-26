package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuyNewCardTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Use gold produced from your mines to buy a new card.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new BuildMageTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
