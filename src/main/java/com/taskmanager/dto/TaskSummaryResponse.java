package com.taskmanager.dto;

/**
 * Response payload for dashboard summary statistics.
 */
public class TaskSummaryResponse {

    private long totalTasks;
    private long pendingTasks;
    private long completedTasks;

    public TaskSummaryResponse() {
    }

    public TaskSummaryResponse(long totalTasks, long pendingTasks, long completedTasks) {
        this.totalTasks = totalTasks;
        this.pendingTasks = pendingTasks;
        this.completedTasks = completedTasks;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }
}
