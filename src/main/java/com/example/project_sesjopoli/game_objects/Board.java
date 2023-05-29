package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;

public class Board {
    public static final int FIELD_HEIGHT = 350;
    private ArrayList<Field> fields = new ArrayList<Field>();

    public Board(){
        fields.add(new Field(1650,1650,350, FIELD_HEIGHT, "START"));

        fields.add(new Field(1400,1650,250,FIELD_HEIGHT, "WWW"));
        fields.add(new Field(1140,1650,260,FIELD_HEIGHT, "GRAFIKA KOMPUTEROWA"));
        fields.add(new Field(880,1650,260,FIELD_HEIGHT, "QUIZ"));
        fields.add(new Field(620,1650,260,FIELD_HEIGHT, "PROGRAMOWANIE SKRYPTOWE"));
        fields.add(new Field(360,1650,260,FIELD_HEIGHT, "BAZY DANYCH"))
        ;
        fields.add(new Field(0,1650,350,FIELD_HEIGHT, "POPRAWKA"));

        fields.add(new Field(0,1350,260,FIELD_HEIGHT, "TPI"));
        fields.add(new Field(0,1090,260,FIELD_HEIGHT, "ALGORYTMY"));
        fields.add(new Field(0,830,350,FIELD_HEIGHT, "MYSTERY BOX"));
        fields.add(new Field(0,570,350,FIELD_HEIGHT, "SW"));
        fields.add(new Field(0,310,260,FIELD_HEIGHT, "AK"));

        fields.add(new Field(0,-40,260,FIELD_HEIGHT, "PARKING"));

        fields.add(new Field(350,-40,350,FIELD_HEIGHT, "INDEKS"));
        fields.add(new Field(600,-40,350,FIELD_HEIGHT, "AM2"));
        fields.add(new Field(860,-40,350,FIELD_HEIGHT, "QUIZ"));
        fields.add(new Field(1120,-40,350,FIELD_HEIGHT, "ALGEBRA"));
        fields.add(new Field(1380,-40,350,FIELD_HEIGHT, "AM1"));

        fields.add(new Field(1650,-40,350,FIELD_HEIGHT, "INNOWACJA"));

        fields.add(new Field(1650,310,350,FIELD_HEIGHT, "ZAHIR"));
        fields.add(new Field(1650,570,350,FIELD_HEIGHT, "MYSTERY BOX"));
        fields.add(new Field(1650,830,350,FIELD_HEIGHT, "PP1"));
        fields.add(new Field(1650,1090,350,FIELD_HEIGHT, "SO2"));
        fields.add(new Field(1650,1350,350,FIELD_HEIGHT, "PP2"));
    }
    public ArrayList<Field> getFields() {
        return fields;
    }
}