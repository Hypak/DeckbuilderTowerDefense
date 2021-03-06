package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class FastforwardTask implements Task {
    public static boolean finished;
    @Override
    public String getTaskText() {
        return "Press tab or the >>> button to speed up battles.";
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
