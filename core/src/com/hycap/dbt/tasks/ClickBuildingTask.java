package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class ClickBuildingTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Click on a building to see its stats.";
    }

    @Override
    public List<Task> getNextTasks() {
        return new ArrayList<>();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
