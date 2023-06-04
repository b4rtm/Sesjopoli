package com.example.project_sesjopoli.game_objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Pawn extends Group {
    public static final double HEIGHT_MULTIPLIER = 0.5;
    public static final int PAWN_WIDTH = 60;
    public static final int PAWN_DEPTH = 60;
    public static final int PAWN_HEIGHT = 80;
    public static final int START_POSITION = 0;

    private final int playerId;
    private int position;

    public Pawn(int id) {
        playerId = id;
        position = START_POSITION;
        List<Color> colors = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE, Color.ORANGE));
        Cylinder pawnBody = new Cylinder(PAWN_WIDTH / 2.0,PAWN_HEIGHT);
        Sphere pawnHead = new Sphere(PAWN_WIDTH / 2.0);
        pawnHead.setTranslateZ(-PAWN_HEIGHT+30);
        Rotate rotateX = new Rotate(90, Rotate.X_AXIS);
        pawnBody.getTransforms().addAll(rotateX);

        this.setTranslateZ(-PAWN_HEIGHT * HEIGHT_MULTIPLIER);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(colors.get(playerId-1));
        pawnBody.setMaterial(material);
        pawnHead.setMaterial(material);
        this.getChildren().add(pawnBody);
        this.getChildren().add(pawnHead);
        this.setVisible(false);
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