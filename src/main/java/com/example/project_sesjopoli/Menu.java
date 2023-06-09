package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.post_objects.PostObjectForSettingName;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.example.project_sesjopoli.GameController.LINK;

public class Menu extends AnchorPane {

    private Label info;
    private Label error;
    private Button playButton;
    private Button exitButton;
    private TextField nameInput;
    private GameController controller;
    private Board board;

    Menu(Stage primaryStage, Scene gameScene, GameController controller, Board board) throws IOException {
        this.setStyle("-fx-background-color: rgb(121, 9, 15)");
        this.controller = controller;
        this.board = board;
        initInfo();
        initError();
        initNameInput();
        initPlayButton(primaryStage, gameScene);
        initExitButton(primaryStage);
        insertAllIntoPane();
    }

    private void insertAllIntoPane() {
        this.getChildren().add(info);
        this.getChildren().add(playButton);
        this.getChildren().add(exitButton);
        this.getChildren().add(nameInput);
        this.getChildren().add(error);
    }

    private void initExitButton(Stage primaryStage) throws IOException {
        FXMLLoader exitButtonLoader = new FXMLLoader(Menu.class.getResource("exitButton.fxml"));
        exitButton = exitButtonLoader.load();
        exitButton.setTextFill(Color.BLACK);
        exitButton.setLayoutX(350);
        exitButton.setLayoutY(440);
        EventHandler<ActionEvent> exit = e -> {
            primaryStage.close();
            System.exit(0);
        };
        exitButton.setOnAction(exit);

        Scale scale = new Scale(1.15, 1.15, 0, 0);
        exitButton.setOnMouseEntered(event -> {
            Bounds bounds = exitButton.getBoundsInLocal();
            double centerX = bounds.getWidth() / 2;
            double centerY = bounds.getHeight() / 2;

            scale.setPivotX(centerX);
            scale.setPivotY(centerY);
            exitButton.getTransforms().add(scale);
        });
        exitButton.setOnMouseExited(event -> {
            exitButton.getTransforms().remove(scale);
        });
    }

    private void initPlayButton(Stage primaryStage, Scene gameScene) throws IOException {
        FXMLLoader playButtonLoader = new FXMLLoader(Menu.class.getResource("playButton.fxml"));
        playButton = playButtonLoader.load();
        playButton.setTextFill(Color.BLACK);
        playButton.setLayoutX(350);
        playButton.setLayoutY(280);
        EventHandler<ActionEvent> play = e -> {
            if(nameInput.getText().length() == 0){
                error.setText("Nie wpisałeś imienia !!!");
            }
            else{
                startGame(primaryStage,gameScene);
            }
        };
        playButton.setOnAction(play);

        Scale scale = new Scale(1.15, 1.15, 0, 0);
        playButton.setOnMouseEntered(event -> {
            Bounds bounds = playButton.getBoundsInLocal();
            double centerX = bounds.getWidth() / 2;
            double centerY = bounds.getHeight() / 2;

            scale.setPivotX(centerX);
            scale.setPivotY(centerY);
            playButton.getTransforms().add(scale);
        });
        playButton.setOnMouseExited(event -> {
            playButton.getTransforms().remove(scale);
        });
    }
    private void startGame(Stage primaryStage, Scene gameScene){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(LINK + "/getplayerid", Integer.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            Integer playerId = responseEntity.getBody();
            if(playerId>=5) {
                error.setText("Osiągnięto limit graczy!");
                return;
            }
            controller.setPlayerId(playerId);
            controller.initThreads();
            controller.getSideScreen().sendPawnInfo(controller, board.getPawns().get(controller.getPlayerId() - 1));
            sendNameToServer();
            primaryStage.setScene(gameScene);
        }
    }
    private void initNameInput() {
        nameInput = new TextField();
        nameInput.setFont(new Font(30));
        nameInput.setPrefWidth(600);
        nameInput.setLayoutX(350);
        nameInput.setLayoutY(160);
        nameInput.setOnKeyTyped(event -> {
            error.setText("");
        });
    }

    private void initError() {
        error = new Label();
        error.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        error.setTextFill(Color.WHITE);
        error.setLayoutX(480);
        error.setLayoutY(580);
    }

    private void initInfo() {
        info = new Label("Podaj swoje imię");
        info.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        info.setTextFill(Color.BLACK);
        info.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10px 60px; -fx-border-width: 6px;");
        info.setLayoutX(385);
        info.setLayoutY(40);
    }
    private void sendNameToServer(){
        RestTemplate restTemplate = new RestTemplate();
        PostObjectForSettingName dataRequest = new PostObjectForSettingName(controller.getPlayerId() - 1,nameInput.getText());
        String responseEntity = restTemplate.postForObject(LINK + "/name", dataRequest, String.class);
    }
}
