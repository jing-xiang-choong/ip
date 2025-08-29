public class EmptyDescriptionException extends EloiseException{
    public EmptyDescriptionException (String command) {
        super("oops! I can't proceed without a description!!\n"
                + "eg. " + command + " borrow book");
    }
}

//public class EmptyDescriptionException extends EloiseException{
//    public EmptyDescriptionException (String missing, String example) {
//        super("oops! I can't proceed without " + missing + "\n"
//                + "eg. " + example);
//    }
//}

