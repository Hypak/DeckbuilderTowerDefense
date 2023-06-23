package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class KillBaseTask implements Task {
    public static boolean finished;
    @Override
    public String getTaskText() {
        return "Place a tower in range of an enemy base to kill it.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new BuildRiftTask());
        return next;
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }
}
