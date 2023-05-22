package com.example.project_sesjopoli;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Game extends Application {

    private static final int BOARD_WIDTH = 2000;
    private static final int BOARD_HEIGHT = 2000;
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 650;
    private static final double ROTATION_SPEED = 100;
    private double mousePosX = 0;

    void setMouseEvents(SubScene scene,SmartGroup group){
        scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
        });

        scene.setOnMouseDragged(event -> {
            double dx = (event.getSceneX() - mousePosX) / scene.getWidth();
            group.rotateByZ(ROTATION_SPEED * dx);
            mousePosX = event.getSceneX();
        });

    }
    void setDefaultBoardPosition(SmartGroup group){
        group.rotateByX(-20);
        group.rotateByZ(-50);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Rectangle board = new Rectangle(BOARD_WIDTH, BOARD_HEIGHT);
        BorderPane wholeScreen = new BorderPane();

        Camera camera = new CameraFactory().initCamera();

        Image image = new Image(getClass().getResourceAsStream("/board.png"));
        ImagePattern imagePattern = new ImagePattern(image);
        board.setFill(imagePattern);

        SmartGroup boardGroup = new SmartGroup();
        boardGroup.getChildren().add(board);

        SubScene mainScene = new SubScene(boardGroup, WIDTH - 400, HEIGHT);
        mainScene.setCamera(camera);
        mainScene.setFill(Color.rgb(121, 9, 15));
        wholeScreen.setLeft(mainScene);

        BorderPane sideScreen = new BorderPane();
        SubScene sideScene = new SubScene(sideScreen, 400, HEIGHT);
        sideScene.setFill(Color.rgb(121, 9, 15));
        wholeScreen.setRight(sideScene);

        setMouseEvents(mainScene,boardGroup);

        setDefaultBoardPosition(boardGroup);

        Scene scene = new Scene(wholeScreen, WIDTH, HEIGHT);

        primaryStage.setTitle("SESJOPOLI");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}