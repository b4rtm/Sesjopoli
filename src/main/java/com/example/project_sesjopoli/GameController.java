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

    public void endTurn(Label l) {
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

    public void getTurn(Label l) {
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

                    Platform.runLater(() -> {
                        l.setText("Tura gracza: " + x);
                    });
                    connection2.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }
}