package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class UpgradeTask implements Task {
    public static boolean finished;
    @Override
    public String getTaskText() {
        return "Use gold to upgrade one of the towers in your base.";
    }

    @Override
    public List<Task> getNextTasks() {
        return new ArrayList<>();
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }
}
