package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

public class Pawn extends Box {
    public static final double HEIGHT_MULTIPLIER = 0.5;
    public static final int START_POSITION = 0;
    private int playerId;
    private Point2D cords;
    private int position;
    private Field actualField;
    private ArrayList<Field> fields;
    

    public Pawn(Board board, int w, int d, int h, int id) {
        super(w,d,h);
        this.setTranslateZ(-h * HEIGHT_MULTIPLIER);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        this.setMaterial(material);
        playerId = id;
        position = START_POSITION;
        fields = board.getFields();
        actualField = fields.get(position);
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

    public ArrayList<Field> getFields() {
        return fields;
    }
}