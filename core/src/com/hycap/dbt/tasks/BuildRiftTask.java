package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public class BuildRiftTask implements Task {
    public static boolean finished = false;
    @Override
    public String getTaskText() {
        return "Build over a rift to generate more energy.";
    }

    @Override
    public List<Task> getNextTasks() {
        List<Task> next = new ArrayList<>();
        return next;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
