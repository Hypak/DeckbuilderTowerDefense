package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.List;

public final class TaskManager {
    private static final List<Task> tasks;
    static {
        tasks = new ArrayList<>();
        tasks.add(new MoveTask());
    }

    private TaskManager() {
    }

    public static void update() {
        for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i).isFinished()) {
                tasks.addAll(tasks.get(i).getNextTasks());
                tasks.remove(i);
                --i;
            }
        }
    }
    public static String getAllTaskDescriptions() {
        if (tasks.isEmpty()) {
            return "";
        }
        final StringBuilder string = new StringBuilder("Tasks:\n");
        for (final Task task : tasks) {
            string.append("\t-").append(task.getTaskText()).append("\n");
        }
        return string.toString();
    }
}
