package eloise.task;

import java.time.LocalDateTime;

/**
 * Represents a task with description
 */
public class ToDo extends Task{

    /**
     * Constructs a todo task with given description
     * @param description the description of the task
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task for display
     *
     * @return example {@code [T][ ] play games}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns string representation of todo task for task saving.
     * <p>
     * Format: T|{doneFlag}|{description}
     * @return string representation of todo task for persistence
     */
    @Override
    public String toFileString() {
        return String.join("|", "T", doneFlag(), description);}
}

