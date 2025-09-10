import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateParser {
    private DateParser() {}

    public static final class Result {
        public final LocalDateTime dateTime;
        public final boolean hasTime;

        private Result (LocalDateTime dt, boolean hasTime) {
            this.dateTime = dt;
            this.hasTime = hasTime;
        }

        public static Result of(LocalDateTime dt, boolean hasTime) {
            return new Result(dt, hasTime);
        }

    }

    /**
     * Only accepts date only events in these formats from user input
     */
    private static final List<DateTimeFormatter> DATE_ONLY = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    );

    /**
     * Only accepts date and time of events in these formats from user input
     */
    private static final List<DateTimeFormatter> DATE_TIME = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm")
    );

    public static Result parser(String userInput) throws EloiseException{
        String trimInput = userInput.trim();

        for (DateTimeFormatter f : DATE_TIME) {
            try {
                LocalDateTime dt = LocalDateTime.parse(trimInput, f);
                return Result.of(dt, true);
            } catch (DateTimeParseException e) {
                //ignored, go to next case of DATE_TIME
            }
        }

        for (DateTimeFormatter f : DATE_ONLY) {
            try {
                LocalDate date = LocalDate.parse(trimInput, f);
                return Result.of(date.atTime(23,59), false);
            } catch (DateTimeParseException e) {

            }
        }

        throw new EloiseException("Invalid date/time input: " + trimInput
                + "\nTry these formats: 2025-09-02, 2025-09-02 1800, 2/9/2025, 2/9/2025 1800");
    }


}
