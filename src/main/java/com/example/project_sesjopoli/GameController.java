package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Field;
import com.example.project_sesjopoli.game_objects.Pawn;
import com.example.project_sesjopoli.game_objects.SubjectField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GameController {

    public static final String LINK = "http://localhost:8080";
    private int playerId;
    private int turn;
    private boolean moved;
    private Board board;
    private SmartGroup boardGroup;
    private SideScreen sideScreen;

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
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            playerId = -1;
        }
    }

    public int getPlayerId() {
        return playerId;
    }
    public int getTurn() {
        return turn;
    }

    public void sendPositionUpdateToServer(int playerId, int field) {
        try {
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(LINK + "/update"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"field\": " + field + ", " + "\"playerId\": " + playerId + "}"))
                    .build();

            System.out.println("{\"field\": " + field + ", " + "\"playerId\": " + playerId + "}");
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postRes = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(postRes.body());
            moved = true;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getPlayersPositionsFromServer(ArrayList<Pawn> pawns) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                try {
                    String endpointUrl2 = LINK + "/getpositions";
                    URL url2 = new URL(endpointUrl2);
                    HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                    connection2.setRequestMethod("GET");
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    Type collectionType = new TypeToken<ArrayList<Integer>>() {
                    }.getType();
                    Gson gson = new Gson();
                    ArrayList<Integer> actualPositions = gson.fromJson(reader2, collectionType);
                    connection2.disconnect();

                    movePawns(actualPositions, pawns);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        };


        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    public void endTurnOnServer() {
        moved=false;
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

    public void getTurnFromServer(SideScreen s) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                try {
                    String endpointUrl2 = LINK + "/getturn";
                    URL url2 = new URL(endpointUrl2);
                    HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                    connection2.setRequestMethod("GET");
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    Type collectionType = new TypeToken<Integer>() {
                    }.getType();
                    Gson gson = new Gson();
                    int x = gson.fromJson(reader2, collectionType);

                    connection2.disconnect();
                    turn = x;

                    if (x == playerId) {
                        turnOnButtons(s);
                    } else {
                        turnOffButtons(s);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }


    public void sendPurchaseInformation(int playerId, int fieldNumber) { //POST
        RestTemplate restTemplate = new RestTemplate();
        DataResponse dataRequest = new DataResponse(playerId,fieldNumber);
        try {
            DataResponse dataResponse = restTemplate.postForObject(LINK + "/house", dataRequest, DataResponse.class);
        }
        catch (Exception e){
            System.out.println("tu jest wszystko dobrze");
        }
    }

    public void checkHouse() {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<DataResponse> responseEntity = restTemplate.getForEntity(LINK + "/house", DataResponse.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {

                    DataResponse dataResponse = responseEntity.getBody();
                    int receivedPlayerID = dataResponse.getPlayerID();
                    int receivedFieldNumber = dataResponse.getFieldNumber();


                    Pawn owner=null;
                    Field tempField =  board.getFields().get(receivedFieldNumber);
                    if(tempField instanceof SubjectField){
                        SubjectField subjectField = (SubjectField) tempField;
                        owner = subjectField.getOwner();
                    }

                    if(owner == null && receivedFieldNumber != 0){ // jesli pole nie ma wlasciciela to wtedy następuje kupno pola
                        Platform.runLater(() -> {

                            Box box = new Box(150, 150, 100);
                            box.setTranslateZ(-100 * 0.5);
                            PhongMaterial material = new PhongMaterial();
                            material.setDiffuseColor(Color.BLUE);
                            box.setMaterial(material);
                            boardGroup.getChildren().add(box);
                            Field actualField = board.getFields().get(receivedFieldNumber);
                            Point2D cords = actualField.getPlace(receivedPlayerID+1); //todo gdzie ma stanąć domek
                            box.setTranslateX(cords.getX());
                            box.setTranslateY(cords.getY());


                            SubjectField field = (SubjectField) board.getFields().get(receivedFieldNumber);   //zakup
                            field.buyField(board.getPawns().get(receivedPlayerID));

                            updateMoneyPanel();

                        });

                    }

                    System.out.println("id = " + receivedPlayerID + " num " + receivedFieldNumber);
                }
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    private void updateMoneyPanel() {
        for(Pawn pawn: board.getPawns()){
            Label label = (Label) sideScreen.moneyPane.getChildren().get(pawn.getPlayerId()-1);
            label.setText("gracz " + pawn.getPlayerId() + " ma " + pawn.getEctsPoints() + "ects");
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
        });
    }

    private void movePawns(ArrayList<Integer> actualPositions, ArrayList<Pawn> pawns) {
        for (int i = 0; i < pawns.size(); ++i) {
            if (pawns.get(i).getPosition() < actualPositions.get(i)) pawns.get(i).makeMove();
        }
    }

    public void setSideScreen(SideScreen sideScreen) {
        this.sideScreen = sideScreen;
    }
}