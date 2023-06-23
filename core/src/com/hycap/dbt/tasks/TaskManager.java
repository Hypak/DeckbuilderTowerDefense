package com.hycap.dbt.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TaskManager {
    private static final List<Task> activeTasks;
    private static final List<Task> completedTasks;
    static {
        activeTasks = new ArrayList<>();
        activeTasks.add(new MoveTask());
        completedTasks = new ArrayList<>();
    }

    private TaskManager() {
    }

    private static void completeTask(int index) {
        completedTasks.add(activeTasks.get(index));
        activeTasks.remove(index);
    }

    public static void update() {
        for (int i = 0; i < activeTasks.size(); ++i) {
            if (activeTasks.get(i).isCompleted()) {
                activeTasks.addAll(activeTasks.get(i).getNextTasks());
                completeTask(i);
                --i;
            }
        }
    }
    public static String getAllActiveTaskDescriptions() {
        return getAllTaskDescriptions(activeTasks, "Tasks:\n");
    }

    public static String getAllCompletedTaskDescriptions() {
        return getAllTaskDescriptions(completedTasks, "Completed Tasks:\n");
    }

    private static String getAllTaskDescriptions(Collection<Task> tasks, String startingString) {
        if (tasks.isEmpty()) {
            return "";
        }
        final StringBuilder string = new StringBuilder(startingString);
        for (final Task task : tasks) {
            string.append("\t-").append(task.getTaskText()).append("\n");
        }
        return string.toString();
    }
}
