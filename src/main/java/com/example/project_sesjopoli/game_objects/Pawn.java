package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;

public class Pawn extends Box {
    public static final double HEIGHT_MULTIPLIER = 0.5;
    public static final int PAWN_WIDTH = 60;
    public static final int PAWN_DEPTH = 60;
    public static final int PAWN_HEIGHT = 80;

    public static final int START_POSITION = 0;
    public static final int INITIAL_ECTS_POINTS = 30;

    private String name;
    private int playerId;
    private Point2D cords;
    private int position;
    private Field actualField;
    private ArrayList<Field> fields;

    private int ectsPoints;

    private ArrayList<SubjectField> boughtFields = new ArrayList<>();


    public Pawn(Board board, int id) {
        super(PAWN_WIDTH, PAWN_DEPTH, PAWN_HEIGHT);
        this.setTranslateZ(-PAWN_HEIGHT * HEIGHT_MULTIPLIER);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        this.setMaterial(material);
        playerId = id;
        position = START_POSITION;
        fields = board.getFields();
        actualField = fields.get(position);
        cords = actualField.getPlace(playerId);
        ectsPoints = INITIAL_ECTS_POINTS;
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

    public ArrayList<SubjectField> getBoughtFields() {
        return boughtFields;
    }

    public int getEctsPoints() {
        return ectsPoints;
    }

    public void setEctsPoints(int ectsPoints) {
        this.ectsPoints = ectsPoints;
    }
}