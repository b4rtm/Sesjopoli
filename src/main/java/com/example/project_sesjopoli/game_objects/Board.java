package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;

public class Board {
    private ArrayList<Field> fields = new ArrayList<Field>();

    public Board(){
        fields.add(new Field(1650,1650, "START"));

        fields.add(new Field(1400,1650, "WWW"));
        fields.add(new Field(1140,1650, "GRAFIKA KOMPUTEROWA"));
        fields.add(new Field(880,1650, "QUIZ"));
        fields.add(new Field(620,1650, "PROGRAMOWANIE SKRYPTOWE"));
        fields.add(new Field(360,1650, "BAZY DANYCH"));

        fields.add(new Field(0,1650, "POPRAWKA"));

        fields.add(new Field(0,1350, "TPI"));
        fields.add(new Field(0,1090, "ALGORYTMY"));
        fields.add(new Field(0,830, "MYSTERY BOX"));
        fields.add(new Field(0,570, "SW"));
        fields.add(new Field(0,310, "AK"));

        fields.add(new Field(0,-40, "PARKING"));

        fields.add(new Field(350,-40, "INDEKS"));
        fields.add(new Field(600,-40, "AM2"));
        fields.add(new Field(860,-40, "QUIZ"));
        fields.add(new Field(1120,-40, "ALGEBRA"));
        fields.add(new Field(1380,-40, "AM1"));

        fields.add(new Field(1650,-40, "INNOWACJA"));

        fields.add(new Field(1650,310, "ZAHIR"));
        fields.add(new Field(1650,570, "MYSTERY BOX"));
        fields.add(new Field(1650,830,"PP1"));
        fields.add(new Field(1650,1090, "SO2"));
        fields.add(new Field(1650,1350, "PP2"));
    }
    public ArrayList<Field> getFields() {
        return fields;
    }
}