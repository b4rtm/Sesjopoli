package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game extends Application {

    public static final int BOARD_WIDTH = 2000;
    public static final int BOARD_HEIGHT = 2000;
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 650;
    public static final double ROTATION_SPEED = 100;
    public static final int SCROLL_SPEED_MULTIPLIER = -3;
    public static final int DEFAULT_BOARD_ANGLE = -40;
    public static final int SIDE_SCENE_WIDTH = 400;
    public static final int MAX_PLAYERS_IN_GAME = 4;
    private double mousePosX = 0;

    public static final double MAX_DISTANCE = 1840.0;
    public static final double MIN_DISTANCE = -1120.0;

    MediaPlayer mediaPlayer;

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
        SubScene mainScene = new SubScene(boardGroup, WIDTH - 400, HEIGHT,true,SceneAntialiasing.BALANCED);
        mainScene.setCamera(camera);
        mainScene.setFill(Color.rgb(121, 9, 15));
        return mainScene;
    }
    SubScene initSideScene(SideScreen sideScreen) {
        SubScene sideScene = new SubScene(sideScreen, SIDE_SCENE_WIDTH, HEIGHT);
        return sideScene;
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

        ArrayList<Pawn> pawns = new ArrayList<>();
        for(int pawnIterator = 1; pawnIterator<= MAX_PLAYERS_IN_GAME; pawnIterator++){
            Pawn pawn = new Pawn(pawnIterator);
            pawns.add(pawn);
            boardGroup.getChildren().add(pawn);
        }

        return pawns;
    }

    private void playMusic() {
        String musicFile = getClass().getResource("lady-of-the-80.mp3").toString();
        Media media = new Media(musicFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.04);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        playMusic();

        SmartGroup boardGroup = initBoardGroup();
        Board board = new Board();
        ArrayList<Pawn> pawns = initPawns(boardGroup, board);
        board.setPawns(pawns);

        GameController controller = new GameController(boardGroup, board);

        Camera camera = new CameraFactory().initCamera();

        BorderPane wholeScreen = new BorderPane();
        SideScreen sideScreen = new SideScreen(controller, board);
        SubScene mainScene = initMainScene(camera, boardGroup);
        SubScene sideScene = initSideScene(sideScreen);

        controller.setSideScreen(sideScreen);


        setMouseEvents(mainScene,boardGroup);

        wholeScreen.setLeft(mainScene);
        wholeScreen.setRight(sideScene);

        Scene gameScene = new Scene(wholeScreen, WIDTH, HEIGHT);

        Menu menuLayout = new Menu(primaryStage, gameScene, controller, board);
        Scene menuScene = new Scene(menuLayout, WIDTH, HEIGHT);

        initPrimaryStage(primaryStage, menuScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}