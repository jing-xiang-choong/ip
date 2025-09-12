package eloise.exception;

public class InvalidIndexException extends EloiseException{
    public InvalidIndexException(String missing) {
        super("Invalid input: " + missing);
    }

    public InvalidIndexException(String missing, int size) {
        super("Invalid input: " + missing + ". You have " + size + " tasks right now");
    }
}
