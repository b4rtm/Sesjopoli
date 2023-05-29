package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Field {
    public static final int X_ADD_RIGHT = 155;
    public static final int X_ADD_LEFT = 90;
    public static final int Y_ADD_DOWN = 180;
    public static final int Y_ADD_UP = 245;
    private ArrayList<Point2D> positions = new ArrayList<>();
    private String name;

    public Field(int x, int y, String name) {
        this.name = name;
        positions.add(new Point2D(x+ X_ADD_LEFT,y+Y_ADD_DOWN));
        positions.add(new Point2D(x+X_ADD_LEFT,y+Y_ADD_UP));
        positions.add(new Point2D(x+X_ADD_RIGHT,y+ Y_ADD_DOWN));
        positions.add(new Point2D(x+ X_ADD_RIGHT,y+ Y_ADD_UP));
    }

    public Point2D getPlace(int id){
        Point2D cords = positions.get(id-1);
        return cords;
    }

    public String getName() {
        return name;
    }

}