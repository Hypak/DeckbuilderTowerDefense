package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuildTowerTask implements Task {
    public static boolean finished;
    @Override
    public String getTaskText() {
        return "Build a tower to defend your base.";
    }

    @Override
    public List<Task> getNextTasks() {
        final List<Task> next = new ArrayList<>();
        next.add(new KillEnemyTask());
        next.add(new UpgradeTask());
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
