package com.example.project_sesjopoli;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends Application {

    private static final int BOARD_WIDTH = 2000;
    private static final int BOARD_HEIGHT = 2000;
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 650;
    private static final double ROTATION_SPEED = 100;
    private double mousePosX = 0;

    public static final double MAX_DISTANCE = 1840.0;
    public static final double MIN_DISTANCE = -1120.0;

    boolean checkDistances(double movement, SmartGroup group){
        if (group.getTranslateZ() >= MAX_DISTANCE && movement < 0
                || group.getTranslateZ() <= MIN_DISTANCE && movement > 0){
            return true;
        }
        return false;
    }

    void setMouseEvents(SubScene scene,SmartGroup group){
        scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
        });

        scene.setOnMouseDragged(event -> {
            double dx = (event.getSceneX() - mousePosX) / scene.getWidth();
            group.rotateByZ(ROTATION_SPEED * dx);
            mousePosX = event.getSceneX();
        });

        scene.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            if (checkDistances(scrollEvent.getDeltaY(),group)){
                return;
            }
            group.translateZProperty().set(group.getTranslateZ() + (-3) * scrollEvent.getDeltaY());

        });

    }
    void setDefaultBoardPosition(SmartGroup group){
        group.rotateByX(-40);
    }

    void initPrimaryStage(Stage primaryStage, Scene scene){
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.setTitle("SESJOPOLI");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    SubScene initMainScene(Camera camera, SmartGroup boardGroup) {
        SubScene mainScene = new SubScene(boardGroup, WIDTH - 400, HEIGHT);
        mainScene.setCamera(camera);
        mainScene.setFill(Color.rgb(121, 9, 15));
        return mainScene;
    }
    SubScene initSideScene(SideScreen sideScreen) {
        SubScene sideScene = new SubScene(sideScreen, 400, HEIGHT);
        return sideScene;
    }

    void initComunicationThread(GameController controller, SideScreen sideScreen) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> controller.getTurn(sideScreen.getTestLabel()), 0, 1, TimeUnit.SECONDS);
    }

    void setBoardImage(Rectangle board){
        Image image = new Image(getClass().getResourceAsStream("/board.png"));
        ImagePattern imagePattern = new ImagePattern(image);
        board.setFill(imagePattern);
    }

    SmartGroup initBoardGroup() {
        Rectangle board = new Rectangle(BOARD_WIDTH, BOARD_HEIGHT);
        setBoardImage(board);

        SmartGroup boardGroup = new SmartGroup();
        boardGroup.getChildren().add(board);
        setDefaultBoardPosition(boardGroup);
        return boardGroup;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameController controller = new GameController();

        SmartGroup boardGroup = initBoardGroup();

        Camera camera = new CameraFactory().initCamera();

        BorderPane wholeScreen = new BorderPane();
        SideScreen sideScreen = new SideScreen(controller);
        SubScene mainScene = initMainScene(camera, boardGroup);
        SubScene sideScene = initSideScene(sideScreen);

        setMouseEvents(mainScene,boardGroup);

        wholeScreen.setLeft(mainScene);
        wholeScreen.setRight(sideScene);

        initComunicationThread(controller, sideScreen);

        Scene scene = new Scene(wholeScreen, WIDTH, HEIGHT);
        initPrimaryStage(primaryStage, scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}