package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

public class Pawn extends Box {
    public static final double HEIGHT_MULTIPLIER = 0.5;
    private int playerId;
    private Point2D cords;
    private int position;
    private Field actualField;
    

    public Pawn(Board board, int w, int d, int h, int id) {
        super(w,d,h);
        this.setTranslateZ(-h * HEIGHT_MULTIPLIER);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        this.setMaterial(material);
        playerId = id;
        position = 0;
        actualField = board.getFields().get(position);
        cords = actualField.getPlace(playerId);
    }

    public int getPosition() {
        return position;
    }

    public void makeMove(){
        position++;
    }

    public Point2D getCords() {
        return cords;
    }

    public int getPlayerId() {
        return playerId;
    }
}