package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Field;
import com.example.project_sesjopoli.game_objects.Pawn;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends Application {

    public static final int BOARD_WIDTH = 2000;
    public static final int BOARD_HEIGHT = 2000;
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 650;
    public static final double ROTATION_SPEED = 100;
    public static final int SCROLL_SPEED_MULTIPLIER = -3;
    public static final int DEFAULT_BOARD_ANGLE = -40;
    public static final int SIDE_SCENE_WIDTH = 400;
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
            group.translateZProperty().set(group.getTranslateZ() + SCROLL_SPEED_MULTIPLIER * scrollEvent.getDeltaY());

        });

    }
    void setDefaultBoardPosition(SmartGroup group){
        group.rotateByX(DEFAULT_BOARD_ANGLE);
    }

    void initPrimaryStage(Stage primaryStage, Scene scene, int playerId){
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.setTitle("SESJOPOLI - " + playerId);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    SubScene initMainScene(Camera camera, SmartGroup boardGroup) {
        SubScene mainScene = new SubScene(boardGroup, WIDTH - 400, HEIGHT,true,SceneAntialiasing.BALANCED);
        mainScene.setCamera(camera);
        mainScene.setFill(Color.rgb(121, 9, 15));
        return mainScene;
    }
    SubScene initSideScene(SideScreen sideScreen) {
        SubScene sideScene = new SubScene(sideScreen, SIDE_SCENE_WIDTH, HEIGHT);
        return sideScene;
    }

    void initThreads(GameController controller, SideScreen sideScreen, ArrayList<Pawn> pawns, Board board) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(() -> {
            controller.getTurnFromServer(sideScreen);
        }, 0, 500, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            controller.getPlayersPositionsFromServer(pawns);
        }, 0, 500, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            drawPawns(pawns, board);
        }, 0, 50, TimeUnit.MILLISECONDS);
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

    private static ArrayList<Pawn> initPawns(SmartGroup boardGroup, Board board) {
        Pawn pawn1 = new Pawn(board, 60,60,80, 1);
        boardGroup.getChildren().add(pawn1);
        Pawn pawn2 = new Pawn(board, 60,60,80, 2);
        boardGroup.getChildren().add(pawn2);
        Pawn pawn3 = new Pawn(board, 60,60,80, 3);
        boardGroup.getChildren().add(pawn3);
        Pawn pawn4 = new Pawn(board, 60,60,80, 4);
        boardGroup.getChildren().add(pawn4);

        ArrayList<Pawn> pawns =new ArrayList<Pawn>();
        pawns.add(pawn1);
        pawns.add(pawn2);
        pawns.add(pawn3);
        pawns.add(pawn4);
        return pawns;
    }

    public void drawPawns(ArrayList<Pawn> pawns, Board board){
        Platform.runLater(() -> {
            for (int i = 0; i < pawns.size(); ++i){
                Field actualField = board.getFields().get(pawns.get(i).getPosition());
                Point2D cords = actualField.getPlace(pawns.get(i).getPlayerId());
                pawns.get(i).setTranslateX(cords.getX());
                pawns.get(i).setTranslateY(cords.getY());
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameController controller = new GameController();

        SmartGroup boardGroup = initBoardGroup();
        Board board = new Board();
        ArrayList<Pawn> pawns = initPawns(boardGroup, board);

        Camera camera = new CameraFactory().initCamera();

        BorderPane wholeScreen = new BorderPane();
        SideScreen sideScreen = new SideScreen(controller, pawns.get(controller.getPlayerId() - 1), board);
        SubScene mainScene = initMainScene(camera, boardGroup);
        SubScene sideScene = initSideScene(sideScreen);

        setMouseEvents(mainScene,boardGroup);

        wholeScreen.setLeft(mainScene);
        wholeScreen.setRight(sideScene);

        initThreads(controller, sideScreen, pawns, board);

        Scene scene = new Scene(wholeScreen, WIDTH, HEIGHT);
        initPrimaryStage(primaryStage, scene, controller.getPlayerId());
    }

    public static void main(String[] args) {
        launch(args);
    }
}