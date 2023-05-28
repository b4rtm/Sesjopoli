package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;

public class Board {
    private ArrayList<Field> fields = new ArrayList<Field>();

    public Board(){
        fields.add(new Field(1650,1650,350,350, "START"));
        fields.add(new Field(1400,1650,250,350, "WWW"));
        fields.add(new Field(1140,1650,260,350, "GRAFIKA KOMPUTEROWA"));
        fields.add(new Field(880,1650,260,350, "QUIZ"));
        fields.add(new Field(620,1650,260,350, "PROGRAMOWANIE SKRYPTOWE"));
        fields.add(new Field(360,1650,260,350, "BAZY DANYCH"));
        fields.add(new Field(0,1650,350,350, "POPRAWKA"));
        fields.add(new Field(0,1350,260,350, "TPI"));
        fields.add(new Field(0,1090,260,350, "ALGORYTMY"));
        fields.add(new Field(0,830,350,350, "MYSTERY BOX"));
        fields.add(new Field(0,570,350,350, "SW"));
        fields.add(new Field(0,310,260,350, "AK"));
        fields.add(new Field(0,-40,260,350, "PARKING"));
        fields.add(new Field(350,-40,350,350, "INDEKS"));
        fields.add(new Field(600,-40,350,350, "AM2"));
        fields.add(new Field(860,-40,350,350, "QUIZ"));
        fields.add(new Field(1120,-40,350,350, "ALGEBRA"));
        fields.add(new Field(1380,-40,350,350, "AM1"));
        fields.add(new Field(1650,-40,350,350, "INNOWACJA"));
        fields.add(new Field(1650,310,350,350, "ZAHIR"));
        fields.add(new Field(1650,570,350,350, "MYSTERY BOX"));
        fields.add(new Field(1650,830,350,350, "PP1"));
        fields.add(new Field(1650,1090,350,350, "SO2"));
        fields.add(new Field(1650,1350,350,350, "PP2"));
    }
    public ArrayList<Field> getFields() {
        return fields;
    }
}