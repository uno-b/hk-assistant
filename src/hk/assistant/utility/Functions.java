package hk.assistant.utility;

import hk.assistant.ui.main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Functions {

    public static void close (ActionEvent event) {
        Node node = ((Node)event.getSource());
        Stage stage = (Stage)node.getScene().getWindow();
        stage.close();
    }


    private static double xOffset = 0;
    private static double yOffset = 0;
    public static void makeStageDragable(Stage stage) {
        Node node = stage.getScene().getRoot();
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
                stage.setOpacity(0.7f);
            }
        });
        node.setOnDragDone((e) -> {
            stage.setOpacity(1.0f);
        });
        node.setOnMouseReleased((e) -> {
            stage.setOpacity(1.0f);
        });
    }


    public static void makeStageDragable(Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.stage.setX(event.getScreenX() - xOffset);
                Main.stage.setY(event.getScreenY() - yOffset);
                Main.stage.setOpacity(0.7f);
            }
        });
        node.setOnDragDone((e) -> {
            Main.stage.setOpacity(1.0f);
        });
        node.setOnMouseReleased((e) -> {
            Main.stage.setOpacity(1.0f);
        });
    }
}