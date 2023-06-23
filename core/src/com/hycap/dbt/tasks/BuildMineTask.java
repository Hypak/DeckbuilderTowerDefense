package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuildMineTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Play a mine card from your hand.\nBuildings have to be placed next to another building.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new BuildTowerTask());
        return next;
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }
}
