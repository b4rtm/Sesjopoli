package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

public class Pawn extends Box {
    private int playerId;
    private Point2D cords;
    private int position;
    private Field actualField;
    private ArrayList<Field> fields = new ArrayList<Field>();

    public Pawn(Board board, int w, int d, int h, int id) {
        super(w,d,h);
        this.setTranslateZ(-h * 0.5);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        this.setMaterial(material);
        playerId = id;
        position = 0;
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