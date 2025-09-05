import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    private final LocalDateTime by;
    private final boolean hasTime;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final DateTimeFormatter SAVE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter SAVE_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");


    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
    }

    @Override
    public String toString() {
        String when = hasTime ? by.format(DT_FMT) : by.format(DATE_FMT);
        return "[D]" + super.toString() + " (by: " + when + ")";
    }

    public LocalDateTime getBy() {
        return by;
    }

    public boolean getHasTime() {
        return hasTime;
    }

    @Override
    public String toFileString() {
        String byStr = hasTime ? by.format(SAVE_DT) : by.format(SAVE_DATE);
        return String.join("|", "D", doneFlag(), description,
                            byStr,
                            Boolean.toString(hasTime));
    }
}
