package chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class chatClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.setMaxWidth(400);
        stage.setMaxHeight(400);
        stage.setMinWidth(400);
        stage.setMinHeight(400);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
