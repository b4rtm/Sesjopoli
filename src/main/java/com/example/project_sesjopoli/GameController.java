package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Pawn;
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

public class GameController {

    private int playerId;
    private int turn;
    private boolean moved;

    GameController() {
        try {
            String endpointUrl = "http://26.117.220.171:8080/getplayerid";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Type collectionType = new TypeToken<Integer>() {
            }.getType();
            Gson gson = new Gson();
            playerId = gson.fromJson(reader, collectionType);
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
                    .uri(new URI("http://localhost:8080/update"))
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
                    String endpointUrl2 = "http://localhost:8080/getpositions";
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
            String endpointUrl = "http://localhost:8080/endturn";
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
                    String endpointUrl2 = "http://localhost:8080/getturn";
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
            System.out.println("LOKALNIE: " + pawns.get(i).getPosition());
            System.out.println("SERWER: " + actualPositions.get(i));
            if (pawns.get(i).getPosition() < actualPositions.get(i)) pawns.get(i).makeMove();
        }
    }
}