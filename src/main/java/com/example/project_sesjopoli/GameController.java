package com.example.project_sesjopoli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameController {

    private int playerId;
    private int turn;

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
    public int getTurnFromServer() {
        return turn;
    }

    public void endTurnOnServer() {
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
            s.getEndTurnButton().setDisable(false);
        });
    }

    private void turnOffButtons(SideScreen s) {
        Platform.runLater(() -> {
            s.getIsYourTurnLabel().setText("NIE TWOJA TURA");
            s.getEndTurnButton().setDisable(true);
        });
    }
}