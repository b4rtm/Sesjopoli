package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private ArrayList<Field> fields;
    private ArrayList<Pawn> pawns;

    public Board(){
        fields = BoardBuilder.createBoard(this);
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setPawns(ArrayList<Pawn> pawns){
        this.pawns= pawns;
    }


    public ArrayList<Pawn> getPawns() {
        return pawns;
    }
}