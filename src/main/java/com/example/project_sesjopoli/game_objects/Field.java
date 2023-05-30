package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Field {
    public static final int X_OFFSET_RIGHT = 155;
    public static final int X_OFFSET_LEFT = 90;
    public static final int Y_OFFSET_DOWN = 180;
    public static final int Y_OFFSET_UP = 245;
    private List<Point2D> positions = new ArrayList<>();
    private String name;

    protected Board board;

    public Field(int x, int y, String name, Board board) {
        this.name = name;
        this.board = board;
        positions.add(new Point2D(x+ X_OFFSET_LEFT,y+Y_OFFSET_DOWN));
        positions.add(new Point2D(x+X_OFFSET_LEFT,y+Y_OFFSET_UP));
        positions.add(new Point2D(x+X_OFFSET_RIGHT,y+ Y_OFFSET_DOWN));
        positions.add(new Point2D(x+ X_OFFSET_RIGHT,y+ Y_OFFSET_UP));
    }

    public Point2D getPlace(int id){
        Point2D cords = positions.get(id-1);
        return cords;
    }

    public String getName() {
        return name;
    }

}