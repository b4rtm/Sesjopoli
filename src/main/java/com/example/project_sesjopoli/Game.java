package com.example.project_sesjopoli;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Game extends Application {

    private static final int WIDTH = 1300;
    private static final int HEIGHT = 650;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Rectangle board = new Rectangle(2000, 2000);
        BorderPane bp = new BorderPane();

        Image image = new Image(getClass().getResourceAsStream("/planszav5.png"));
        ImagePattern imagePattern = new ImagePattern(image);
        board.setFill(imagePattern);

        Group group = new Group();
        group.getChildren().add(board);

        Camera camera = new PerspectiveCamera(true);

        SubScene sub = new SubScene(group, WIDTH - 400, HEIGHT);
        sub.setCamera(camera);
        sub.setFill(Color.rgb(175, 255, 112));
        bp.setLeft(sub);

        BorderPane bpInfo = new BorderPane();
        bpInfo.setStyle("-fx-background-color: rgb(175, 125, 112);");
        SubScene sub2 = new SubScene(bpInfo, 400, HEIGHT);
        bp.setRight(sub2);

        camera.translateXProperty().set(1000);
        camera.translateYProperty().set(900);
        camera.translateZProperty().set(-4100);
        camera.setNearClip(1);
        camera.setFarClip(5000);

        group.setRotationAxis(Rotate.X_AXIS);
        group.setRotate(-40);

        Scene scene = new Scene(bp, WIDTH, HEIGHT);

        primaryStage.setTitle("SESJOPOLI");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}