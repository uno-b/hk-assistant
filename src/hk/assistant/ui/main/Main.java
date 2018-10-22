package hk.assistant.ui.main;

import hk.assistant.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        this.stage = primaryStage;
        primaryStage.show();

        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}