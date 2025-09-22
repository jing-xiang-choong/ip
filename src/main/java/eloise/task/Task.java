package eloise.task;

import java.time.LocalDateTime;

abstract public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public boolean getIsDone() { return this.isDone; }

    public String getDescription() { return this.description; }

    public String doneFlag() {
        return isDone ? "1" : "0";
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public LocalDateTime getDateTime() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public abstract String toFileString();
}
