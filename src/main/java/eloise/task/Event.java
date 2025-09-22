package eloise.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with specific start and end time.
 * <p>
 * {@code Event} stores task description, start and end time.
 * The start and end times can have specific time or just the date.
 */
public class Event extends Task{

    private final LocalDateTime from;
    private final LocalDateTime to;
    private final boolean hasStartTime;
    private final boolean hasEndTime;


    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final DateTimeFormatter SAVE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter SAVE_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs an Event task with a given description, start and end time.
     *
     * @param description the description of task
     * @param from the {@link LocalDateTime} that represents start time
     * @param to the {@link LocalDateTime} that represents end time
     * @param hasStartTime {@code true} if the start time includes specific time
     *                     {@code false} if it does not
     * @param hasEndTime {@code true} if the end time includes specific time
     *      *            {@code false} if it does not
     */
    public Event(String description, LocalDateTime from, LocalDateTime to,
                 boolean hasStartTime, boolean hasEndTime) {
        super(description);
        this.from = from;
        this.to = to;
        this.hasStartTime = hasStartTime;
        this.hasEndTime = hasEndTime;
    }

    @Override
    public LocalDateTime getDateTime() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public boolean getHasStartTIme() {
        return hasStartTime;
    }

    public boolean getHasEndTime() {
        return hasEndTime;
    }

    /**
     * Returns a string representation of the event task for display
     *
     * @return example {@code [E][ ] play games (from: Sept 16 2025, 11:59PM to: Sept 17 2025, 11:59PM)}
     */
    @Override
    public String toString() {
        String startStr = hasStartTime ? from.format(DT_FMT) : from.format(DATE_FMT);
        String endStr = hasEndTime ? to.format(DT_FMT) : to.format(DATE_FMT);
        return "[E]" + super.toString() + " (from: " + startStr  + " to: " + endStr + ")";
    }

    /**
     * Returns string representation of event task for task saving.
     * <p>
     * Format: E|{doneFlag}|{description}|{fromStr}|{toStr}|hasStartTime|hasEndTime
     * @return string representation of event task for persistence
     */
    @Override
    public String toFileString() {
        String fromStr = hasStartTime ? from.format(SAVE_DT) : from.format(SAVE_DATE);
        String toStr = hasEndTime ? to.format(SAVE_DT) : to.format(SAVE_DATE);

        return String.join("|", "E", doneFlag(),
                description,
                fromStr,
                toStr,
                Boolean.toString(hasStartTime),
                Boolean.toString(hasEndTime));
    }
}
