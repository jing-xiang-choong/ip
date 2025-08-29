public class MissingArgumentException extends EloiseException {
    public MissingArgumentException(String missing, String example) {
        super("Missing " + missing + "\n" +
                "eg. " + example);
    }
}
