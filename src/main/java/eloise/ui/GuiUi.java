package eloise.ui;

public class GuiUi extends Ui {
    private StringBuilder buffer = new StringBuilder();

    @Override
    public void showMessage(String msg) {
        buffer.append(msg).append("\n");
    }

    public String getOutput() {
        String output = buffer.toString();
        buffer.setLength(0); // reset for next round
        return output;
    }
}
