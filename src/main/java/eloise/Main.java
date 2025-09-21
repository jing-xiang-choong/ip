package eloise;

import java.io.IOException;

import eloise.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Eloise using FXML.
 */
public class Main extends Application {

    private final Eloise eloise = new Eloise();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            fxmlLoader.<MainWindow>getController().setEloise(eloise);  // inject the Duke instance
            stage.setTitle("Eloise");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
