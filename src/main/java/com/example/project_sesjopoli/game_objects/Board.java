package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private ArrayList<Field> fields;
    private ArrayList<Pawn> pawns;
    private HashMap<SubjectFieldType, Integer> subjectFieldTypeAppearances;

    public Board(){
        fields = BoardBuilder.createBoard(this);
        subjectFieldTypeAppearances = BoardBuilder.createSubjectFieldsMap();
    }

    public HashMap<SubjectFieldType, Integer> getSubjectFieldTypeAppearances() {
        return subjectFieldTypeAppearances;
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