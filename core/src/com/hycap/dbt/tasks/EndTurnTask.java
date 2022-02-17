package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class EndTurnTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Press e or the end turn button to end your turn.\n" +
                "Ending your turn replenishes your energy\nand you draw a new hand.";
    }

    @Override
    public List<Task> getNextTasks() {
        List<Task> next = new ArrayList<>();
        next.add(new PlayActionTask());
        next.add(new ClickBuildingTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
