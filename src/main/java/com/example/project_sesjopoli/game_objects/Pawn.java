package com.example.project_sesjopoli.game_objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;


public class Pawn extends Box {
    public static final double HEIGHT_MULTIPLIER = 0.5;
    public static final int PAWN_WIDTH = 60;
    public static final int PAWN_DEPTH = 60;
    public static final int PAWN_HEIGHT = 80;
    public static final int START_POSITION = 0;

    private final int playerId;
    private int position;

    public Pawn(int id) {
        super(PAWN_WIDTH, PAWN_DEPTH, PAWN_HEIGHT);
        this.setTranslateZ(-PAWN_HEIGHT * HEIGHT_MULTIPLIER);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        this.setMaterial(material);
        playerId = id;
        position = START_POSITION;
    }

    public int getPosition() {
        return position;
    }

    public void makeMove(){
        position++;
        if(position==24) position=0;
    }

    public int getPlayerId() {
        return playerId;
    }

}