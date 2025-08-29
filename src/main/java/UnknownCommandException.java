public class UnknownCommandException extends EloiseException{
    public UnknownCommandException(String userInput) {
        super("oopsies! I do not recognise " + userInput +  "\n"
        + "Try the commands mentioned above or marking/unmarking your tasks!");
    }
}
