package eloise.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with specific deadline
 * <p>
 * {@code Deadline} stores task description and the deadline.
 * The deadline can have specific time or just the date.
 */
public class Deadline extends Task {

    private final LocalDateTime by;
    private final boolean hasTime;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final DateTimeFormatter SAVE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter SAVE_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");


    /**
     * Constructs a Deadline task with a given description and deadline.
     *
     * @param description the description of task
     * @param by the {@link LocalDateTime} that represents deadline
     * @param hasTime {@code true} if the deadline includes specific time
     *                {@code false} if it does not
     */
    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
    }

    /**
     * Returns a string representation of the deadline task for display
     *
     * @return example {@code [D][ ] play games (by: Sept 16 2025, 11:59PM)}
     */
    @Override
    public String toString() {
        String when = hasTime ? by.format(DT_FMT) : by.format(DATE_FMT);
        return "[D]" + super.toString() + " (by: " + when + ")";
    }

    /**
     * Returns string representation of deadline task for task saving.
     * <p>
     * Format: D|{doneFlag}|{description}|{deadline}|{hasTime}
     * @return string representation of deadline task for persistence
     */
    @Override
    public String toFileString() {
        String byStr = hasTime ? by.format(SAVE_DT) : by.format(SAVE_DATE);
        return String.join("|", "D", doneFlag(), description,
                            byStr,
                            Boolean.toString(hasTime));
    }
}
