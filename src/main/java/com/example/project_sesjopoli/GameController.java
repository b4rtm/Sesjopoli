package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.*;
import com.example.project_sesjopoli.post_objects.PostObjectForAnsweringQuiz;
import com.example.project_sesjopoli.post_objects.PostObjectForBuyingHouse;
import com.example.project_sesjopoli.post_objects.PostObjectForMoving;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class GameController {

    public static final String LINK = "http://localhost:8080";
    private int playerId;
    private boolean moved;
    private Board board;
    private SmartGroup boardGroup;
    private SideScreen sideScreen;
    private ArrayList<Integer> positionsWithHouses;

    GameController(SmartGroup boardGroup, Board board) {
        try {
            String endpointUrl = LINK + "/getplayerid";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Type collectionType = new TypeToken<Integer>() {
            }.getType();
            Gson gson = new Gson();
            playerId = gson.fromJson(reader, collectionType);
            this.board = board;
            this.boardGroup = boardGroup;
            this.positionsWithHouses = new ArrayList<>();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            playerId = -1;
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public void sendPositionUpdateToServer(int playerId, int field) {
        moved = true;
        RestTemplate restTemplate = new RestTemplate();
        PostObjectForMoving dataRequest = new PostObjectForMoving(playerId, field);
        String responseEntity = restTemplate.postForObject(LINK + "/update", dataRequest, String.class);
        System.out.println(responseEntity);
    }

    public void handleGameState(SideScreen s) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<GameState> responseEntity = restTemplate.getForEntity(LINK + "/", GameState.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {

                    GameState current = responseEntity.getBody();
                    hideOrShowButtons(current.whoseTurn, s);
                    movePawns(current.playerPositions);
                    buildHouse(current.positionOwners);
                    updateMoneyPanel(current.money, current.names);
                    if (current.playerLostFlags.get(playerId - 1)){
                        sideScreen.displayLooserInfo();
                    }
                    if (playerWon(current)){
                        sideScreen.disableAllButtons();
                        sideScreen.displayWinnerInfo();

                    }
                    //showQuiz(current.questions);
                }
            }
        };
        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    public boolean playerWon(GameState current){
        return (Collections.frequency(current.playerLostFlags, true) == current.playerId - 1
                && !current.playerLostFlags.get(playerId - 1) && current.playerId > 1);
    }

    public void endTurnOnServer() {
        moved = false;
        try {
            String endpointUrl = LINK + "/endturn";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }


    public void sendPurchaseInformation(int playerId, int fieldNumber) { //POST
        RestTemplate restTemplate = new RestTemplate();
        PostObjectForBuyingHouse dataRequest = new PostObjectForBuyingHouse(playerId, fieldNumber);
        String responseEntity = restTemplate.postForObject(LINK + "/house", dataRequest, String.class);
    }

    public void sendQuizAnswer(int answerIndex, int questionIndex, int playerId) { //POST
        RestTemplate restTemplate = new RestTemplate();
        PostObjectForAnsweringQuiz dataRequest = new PostObjectForAnsweringQuiz(answerIndex, questionIndex, playerId);
        String responseEntity = restTemplate.postForObject(LINK + "/answer", dataRequest, String.class);
    }

    private void updateMoneyPanel(ArrayList<Integer> money, ArrayList<String> names) {
        for (Pawn pawn : board.getPawns()) {
            Platform.runLater(() -> {
                sideScreen.setTextInMoneyPane(pawn.getPlayerId() - 1, money.get(pawn.getPlayerId() - 1), names.get(pawn.getPlayerId() - 1));
            });
        }
    }

    private void hideOrShowButtons(int turn, SideScreen s) {
        if (turn == playerId) {
            turnOnButtons(s);
        } else {
            turnOffButtons(s);
        }
    }

    private void turnOnButtons(SideScreen s) {
        Platform.runLater(() -> {
            s.getIsYourTurnLabel().setText("TWOJA TURA");
            if (!moved) s.getMovePawnButton().setDisable(false);
            s.getEndTurnButton().setDisable(false);
        });
    }

    private void turnOffButtons(SideScreen s) {
        Platform.runLater(() -> {
            s.getIsYourTurnLabel().setText("NIE TWOJA TURA");
            s.getMovePawnButton().setDisable(true);
            s.getEndTurnButton().setDisable(true);
            s.getBuyHousePane().setVisible(false);
            s.getQuizPane().setVisible(false);
        });
    }

    private void movePawns(ArrayList<Integer> actualPositions) {
        for (int i = 0; i < board.getPawns().size(); ++i) {
            if (board.getPawns().get(i).getPosition() != actualPositions.get(i)) board.getPawns().get(i).makeMove();
        }
    }

    private void buildHouse(ArrayList<Integer> positionOwners) {
        Platform.runLater(() -> {
            for (int i = 0; i < positionOwners.size(); ++i) {
                if (positionOwners.get(i) != -1 && !positionsWithHouses.contains(i)) {
                    System.out.println(positionOwners.get(i));
                    House house = new House();
                    boardGroup.getChildren().add(house);
                    Field actualField = board.getFields().get(i);
                    Point2D cords = actualField.getPlace(5);
                    house.setTranslateX(cords.getX());
                    house.setTranslateY(cords.getY());
                    positionsWithHouses.add(i);
                }
            }
        });
    }

    public void setSideScreen(SideScreen sideScreen) {
        this.sideScreen = sideScreen;
    }

    public ArrayList<Integer> getPositionsWithHouses() {
        return positionsWithHouses;
    }
}