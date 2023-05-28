package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Field {
    private ArrayList<Point2D> positions = new ArrayList<>();
    private String name;

    public Field(int x, int y, int width, int height, String name) {
        this.name = name;
        positions.add(new Point2D(x+90,y+180));
        positions.add(new Point2D(x+90,y+245));
        positions.add(new Point2D(x+155,y+180));
        positions.add(new Point2D(x+155,y+245));
    }

    public Point2D getPlace(int id){
        Point2D cords = positions.get(id-1);
        return cords;
    }

    public String getName() {
        return name;
    }

}