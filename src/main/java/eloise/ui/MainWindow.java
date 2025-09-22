package eloise.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import eloise.Eloise;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Eloise eloise;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/jennieImage.jpeg"));
    private Image eloiseImage = new Image(this.getClass().getResourceAsStream("/images/rosieImage.jpeg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setEloise(Eloise eloise) {
        this.eloise = eloise;
    }

    /// used ChatGPT to help me figure out how to add welcome message from CLI to GUI
    public void showWelcome() {
        dialogContainer.getChildren().add(
                DialogBox.getEloiseDialog("Hello, I'm Eloise! Your favourite productivity bot!\n"
                                + "For Todo: enter \"todo <task>\"\n"
                                + "For Deadline: enter \"deadline <task> /by <date/time>\"\n"
                                + "For Event: enter \"event <task> /from <date/time> /to <date/time>\"",
                        eloiseImage));

    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = eloise.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getEloiseDialog(response, eloiseImage)
        );
        userInput.clear();

    }
}



