package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class KillEnemyTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Kill your first enemy.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new FastforwardTask());
        next.add(new KillBaseTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
