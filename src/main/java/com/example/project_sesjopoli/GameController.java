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
    private HashMap<Integer, House> positionsWithHouses;
    private GameState current;

    GameController(SmartGroup boardGroup, Board board) {
        this.board = board;
        this.boardGroup = boardGroup;
        this.positionsWithHouses = new HashMap<>();
    }

    public void drawPawns(Board board) {
        Platform.runLater(() -> {
            for (int i = 0; i < board.getPawns().size(); ++i) {
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
        }, 0, 100, TimeUnit.MILLISECONDS);
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
    }

    public void handleGameState(SideScreen sideScreen) {
        Runnable requestTask = () -> {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GameState> responseEntity = restTemplate.getForEntity(LINK + "/", GameState.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                current = responseEntity.getBody();
                showWhoseTurn(current.whoseTurn, current.playerId, sideScreen);
                hideOrShowButtons(current.whoseTurn, sideScreen);
                movePawns(current.playerPositions);
                buildHouse(current.positionOwners);
                setPawnsVisible(current.playerId, current.playerLostFlags);
                setPlayersLabels(current.playerId, current.money, current.names);
                setHousesNotVisible(current.playerLostFlags);
                if (wasAPenalty(current.punishmentInfo)) {
                    Platform.runLater(() -> {
                       sideScreen.displayPunishmentInfo(current,playerId);
                    });
                }
                if(wasInnovationField(current.punishmentInfo)){
                    Platform.runLater(() -> {
                    GameController.this.sideScreen.displayInnovationInfo(current,playerId);
                    });
                }
                if (current.playerLostFlags.get(playerId - 1)) {
                    GameController.this.sideScreen.displayLooserInfo();
                }
                if (playerWon(current)) {
                    GameController.this.sideScreen.displayWinnerInfo();
                }
            }
        };
        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    public void setHousesNotVisible(ArrayList<Boolean> playerLostFlags){
        for(House house : positionsWithHouses.values()){
            for(int i=0;i< playerLostFlags.size();i++){
                if(playerLostFlags.get(i)){
                    board.getPawns().get(i).setVisible(false);
                }
                if(house.getOwnerId() == i && playerLostFlags.get(i)){
                    house.setVisible(false);
                }
            }
        }
    }


    private boolean wasAPenalty(PunishmentInfo punishmentInfo) {
        return punishmentInfo.getPayerId() != -1 && punishmentInfo.getPayeeId() != -1;
    }

    private boolean wasInnovationField(PunishmentInfo punishmentInfo) {
        return punishmentInfo.getPayerId() == -1 && punishmentInfo.getPayeeId() != -1;
    }

    public void showWhoseTurn(int whoseTurn, int numberOfPlayers, SideScreen sideScreen) {
        Platform.runLater(() -> {
            for (int id = 1; id <= numberOfPlayers; id++) {
                sideScreen.changeDicePosition(id,whoseTurn);
            }
        });
    }

    private void setPlayersLabels(int numberOfPlayers, ArrayList<Integer> money, ArrayList<String> names) {
        Platform.runLater(() -> {
            for (int i = 0; i < numberOfPlayers; ++i) {
                AnchorPane anchorPane = (AnchorPane) sideScreen.playerInfoPane.getChildren().get(i);
                anchorPane.setVisible(true);
                ((Text) anchorPane.getChildren().get(1)).setText(names.get(i));
                ((Text) anchorPane.getChildren().get(2)).setText("ECTS: " + money.get(i));
            }
        });
    }

    public boolean playerWon(GameState current) {
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
        }
    }


    public void sendPurchaseInformation(int playerId, int fieldNumber) { //POST
        RestTemplate restTemplate = new RestTemplate();
        PostObjectForBuyingHouse dataRequest = new PostObjectForBuyingHouse(playerId, fieldNumber);
        String responseEntity = restTemplate.postForObject(LINK + "/house", dataRequest, String.class);
    }

    public void sendExitInformation(Integer playerId) { //POST
        RestTemplate restTemplate = new RestTemplate();
        String responseEntity = restTemplate.postForObject(LINK + "/exit", playerId, String.class);
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
                    House house = new House(positionOwners.get(i));
                    boardGroup.getChildren().add(house);
                    Field actualField = board.getFields().get(i);
                    Point2D cords = actualField.getPlace(5);
                    house.setTranslateX(cords.getX());
                    house.setTranslateY(cords.getY());
                    positionsWithHouses.put(i, house);

                } else if (positionOwners.get(i) == -1 && positionsWithHouses.containsKey(i)) {
                    positionsWithHouses.remove(i);
                }
            }
        });
    }

    private void setPawnsVisible(int numberOfPlayers, ArrayList<Boolean> playersLostFlags) {
        Platform.runLater(() -> {
            for (int i = 0; i < numberOfPlayers; ++i) {
                if(playersLostFlags.get(i) == false)
                    board.getPawns().get(i).setVisible(true);
                else
                    board.getPawns().get(i).setVisible(false);
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

    public GameState getCurrent() {
        return current;
    }
}