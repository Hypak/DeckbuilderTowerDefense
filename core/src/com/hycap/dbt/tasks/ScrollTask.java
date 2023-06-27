package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class ScrollTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Use the scroll wheel to zoom in and out.\nAlternatively use Q Z or PageUp PageDown.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new BuildMineTask());
        next.add(new EndTurnTask());
        return next;
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }
}
