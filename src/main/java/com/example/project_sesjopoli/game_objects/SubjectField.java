package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;

public class SubjectField extends Field{

    public SubjectField(int pawnsBaseX, int pawnsBaseY, Point2D houseBasePoint, String name, Board board) {
        super(pawnsBaseX, pawnsBaseY, name, board);
        this.positions.add(houseBasePoint);
    }
}
