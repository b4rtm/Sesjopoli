package com.example.project_sesjopoli.game_objects;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardBuilder {

    public static ArrayList<Field> createBoard(Board board){
        ArrayList<Field> fields = new ArrayList<>();
        fields.add(new Field(1650,1650, "START", board));

        fields.add(new SubjectField(1400,1650, "WWW",SubjectFieldType.ORANGE,2,2,board));
        fields.add(new SubjectField(1140,1650, "GRAFIKA KOMPUTEROWA",SubjectFieldType.ORANGE,2,2,board));
        fields.add(new Field(880,1650, "QUIZ",board));
        fields.add(new SubjectField(620,1650, "PROGRAMOWANIE SKRYPTOWE",SubjectFieldType.YELLOW,board));
        fields.add(new SubjectField(360,1650, "BAZY DANYCH",SubjectFieldType.YELLOW,board));

        fields.add(new Field(0,1650, "POPRAWKA",board));

        fields.add(new SubjectField(0,1350, "TPI",SubjectFieldType.PURPLE,2,2,board));
        fields.add(new SubjectField(0,1090, "ALGORYTMY",SubjectFieldType.PURPLE,3,3,board));
        fields.add(new Field(0,830, "MYSTERY BOX",board));
        fields.add(new SubjectField(0,570, "SW",SubjectFieldType.GREEN,3,3,board));
        fields.add(new SubjectField(0,310, "AK",SubjectFieldType.GREEN,4,4,board));

        fields.add(new Field(0,-40, "PARKING",board));

        fields.add(new SubjectField(350,-40, "INDEKS",SubjectFieldType.RESTAURANT,2,2,board));
        fields.add(new SubjectField(600,-40, "AM2",SubjectFieldType.BLUE,5,5,board));
        fields.add(new Field(860,-40, "QUIZ",board));
        fields.add(new SubjectField(1120,-40, "ALGEBRA",SubjectFieldType.BLUE,5,5,board));
        fields.add(new SubjectField(1380,-40, "AM1",SubjectFieldType.BLUE,6,6,board));

        fields.add(new Field(1650,-40, "INNOWACJA",board));

        fields.add(new SubjectField(1650,310, "ZAHIR",SubjectFieldType.RESTAURANT,2,2,board));
        fields.add(new Field(1650,570, "MYSTERY BOX",board));
        fields.add(new SubjectField(1650,830,"PP1",SubjectFieldType.RED,6,6,board));
        fields.add(new SubjectField(1650,1090, "SO2",SubjectFieldType.RED,8,8,board));
        fields.add(new SubjectField(1650,1350, "PP2",SubjectFieldType.RED,10,10,board));
        return fields;
    }

    public static HashMap<SubjectFieldType, Integer> createSubjectFieldsMap(){

        HashMap<SubjectFieldType, Integer> subjectFieldTypeAppearances = new HashMap<>();
        subjectFieldTypeAppearances.put(SubjectFieldType.ORANGE, 2);
        subjectFieldTypeAppearances.put(SubjectFieldType.YELLOW, 2);
        subjectFieldTypeAppearances.put(SubjectFieldType.PURPLE, 2);
        subjectFieldTypeAppearances.put(SubjectFieldType.GREEN, 2);
        subjectFieldTypeAppearances.put(SubjectFieldType.BLUE, 3);
        subjectFieldTypeAppearances.put(SubjectFieldType.RED, 3);
        subjectFieldTypeAppearances.put(SubjectFieldType.RESTAURANT, 2);

        return subjectFieldTypeAppearances;
    }
}
