package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Field;
import com.example.project_sesjopoli.game_objects.House;
import com.example.project_sesjopoli.post_objects.PostObjectForAnsweringQuiz;
import com.example.project_sesjopoli.post_objects.PostObjectForBuyingHouse;
import com.example.project_sesjopoli.post_objects.PostObjectForMoving;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController {

    public static final String LINK = "http://localhost:8080";
    private int playerId;
    private boolean moved;
    private Board board;
    private SmartGroup boardGroup;
    private SideScreen sideScreen;
    private HashMap<Integer,House> positionsWithHouses;

    GameController(SmartGroup boardGroup, Board board) {
            this.board = board;
            this.boardGroup = boardGroup;
            this.positionsWithHouses = new HashMap<>();
    }

    public void drawPawns(Board board){
        Platform.runLater(() -> {
            for (int i = 0; i < board.getPawns().size(); ++i){
                Field actualField = board.getFields().get(board.getPawns().get(i).getPosition());
                Point2D cords = actualField.getPlace(board.getPawns().get(i).getPlayerId());
                board.getPawns().get(i).setTranslateX(cords.getX());
                board.getPawns().get(i).setTranslateY(cords.getY());
            }
        });
    }

    void initThreads() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        executorService.scheduleAtFixedRate(() -> {
            handleGameState(sideScreen);
        }, 0, 200, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            drawPawns(board);
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    public void handleGameState(SideScreen sideScreen) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<GameState> responseEntity = restTemplate.getForEntity(LINK + "/", GameState.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {

                    GameState current = responseEntity.getBody();
                    showWhoseTurn(current.whoseTurn,current.playerId, sideScreen);
                    hideOrShowButtons(current.whoseTurn, sideScreen);
                    movePawns(current.playerPositions);
                    buildHouse(current.positionOwners);
                    setPawnsVisible(current.playerId);
                    setPlayersLabels(current.playerId, current.money, current.names);
                    if (current.playerLostFlags.get(playerId - 1)){
                        GameController.this.sideScreen.displayLooserInfo();
                    }
                    if (playerWon(current)){
                        GameController.this.sideScreen.disableAllButtons();
                        GameController.this.sideScreen.displayWinnerInfo();

                    }
                    //showQuiz(current.questions);
                }
            }
        };
        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    public void showWhoseTurn(int whoseTurn, int numberOfPlayers, SideScreen sideScreen){
        Platform.runLater(() -> {
            for(int id=1;id<=numberOfPlayers;id++) {
                ((AnchorPane) sideScreen.playerInfoPane.getChildren().get(id - 1)).getChildren().get(3).setVisible(whoseTurn == id);
            }
        });
    }

    private void setPlayersLabels(int numberOfPlayers, ArrayList<Integer> money, ArrayList<String> names) {
        Platform.runLater(() -> {
            for(int i=0;i<numberOfPlayers;++i){
                AnchorPane anchorPane = (AnchorPane) sideScreen.playerInfoPane.getChildren().get(i);
                anchorPane.setVisible(true);

                ((Text)anchorPane.getChildren().get(1)).setText(names.get(i));
                ((Text)anchorPane.getChildren().get(2)).setText("ECTS: " + money.get(i));
            }
        });
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


    private void hideOrShowButtons(int turn, SideScreen s) {
        if (turn == playerId) {
            turnOnButtons(s);
        } else {
            turnOffButtons(s);
        }
    }

    private void turnOnButtons(SideScreen s) {
        Platform.runLater(() -> {
            if (!moved) s.getMovePawnButton().setVisible(true);
            s.getEndTurnButton().setVisible(true);
        });
    }

    private void turnOffButtons(SideScreen s) {
        Platform.runLater(() -> {
            s.getMovePawnButton().setVisible(false);
            s.getEndTurnButton().setVisible(false);
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
                if (positionOwners.get(i) != -1 && !positionsWithHouses.containsKey(i)) {
                    System.out.println(positionOwners.get(i));
                    House house = new House(positionOwners.get(i));
                    boardGroup.getChildren().add(house);
                    Field actualField = board.getFields().get(i);
                    Point2D cords = actualField.getPlace(5);
                    house.setTranslateX(cords.getX());
                    house.setTranslateY(cords.getY());
                    positionsWithHouses.put(i,house);
                    System.out.println(actualField.getName());
                }
                else if(positionOwners.get(i) == -1 && positionsWithHouses.containsKey(i)){
                    positionsWithHouses.get(i).setVisible(false);
                }
                else if(positionOwners.get(i) != -1 && positionsWithHouses.containsKey(i)){
                    positionsWithHouses.get(i).setVisible(true);
                }
            }
        });
    }
    private void setPawnsVisible(int numberOfPlayers) {
        Platform.runLater(() -> {
            for(int i=0;i<numberOfPlayers;++i){
                board.getPawns().get(i).setVisible(true);
            }
        });
    }

    public void setSideScreen(SideScreen sideScreen) {
        this.sideScreen = sideScreen;
    }

    public SideScreen getSideScreen() {
        return sideScreen;
    }

    public HashMap<Integer, House> getPositionsWithHouses() {
        return positionsWithHouses;
    }
}