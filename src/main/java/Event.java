import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task{

    private final LocalDateTime from;
    private final LocalDateTime to;
    private final boolean hasStartTime;
    private final boolean hasEndTime;


    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final DateTimeFormatter SAVE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter SAVE_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Event(String description, LocalDateTime from, LocalDateTime to,
                 boolean hasStartTime, boolean hasEndTime) {
        super(description);
        this.from = from;
        this.to = to;
        this.hasStartTime = hasStartTime;
        this.hasEndTime = hasEndTime;
    }

    public LocalDateTime getFrom() {
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

    @Override
    public String toString() {
        String startStr = hasStartTime ? from.format(DT_FMT) : from.format(DATE_FMT);
        String endStr = hasEndTime ? to.format(DT_FMT) : to.format(DATE_FMT);
        return "[E]" + super.toString() + " (from: " + startStr  + " to: " + endStr + ")";
    }

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
