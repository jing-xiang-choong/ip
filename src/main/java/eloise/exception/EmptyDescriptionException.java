package eloise.exception;

public class EmptyDescriptionException extends EloiseException{
    public EmptyDescriptionException (String command) {
        super("oops! I can't proceed without a description!!\n"
                + "eg. " + command + " borrow book");
    }
}



