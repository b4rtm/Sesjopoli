package com.example.project_sesjopoli.game_objects;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardBuilder {

    public static ArrayList<Field> createBoard(Board board){
        ArrayList<Field> fields = new ArrayList<>();
        fields.add(new Field(1650,1650, "START", board));

        fields.add(new SubjectField(1400,1660, new Point2D(1440,1690), "WWW",board));
        fields.add(new SubjectField(1140,1660, new Point2D(1180,1690), "GRAFIKA KOMPUTEROWA",board));
        fields.add(new Field(880,1650, "QUIZ",board));
        fields.add(new SubjectField(620,1660, new Point2D(660,1690), "PROGRAMOWANIE SKRYPTOWE",board));
        fields.add(new SubjectField(360,1660, new Point2D(400,1690), "BAZY DANYCH",board));

        fields.add(new Field(0,1650, "POPRAWKA",board));

        fields.add(new SubjectField(30,1350, new Point2D(310,1450), "TPI",board));
        fields.add(new SubjectField(30,1090, new Point2D(310,1190), "ALGORYTMY",board));
        fields.add(new Field(30,830, "MYSTERY BOX",board));
        fields.add(new SubjectField(30,570, new Point2D(310,670), "SW",board));
        fields.add(new SubjectField(30,310, new Point2D(310,410), "AK",board));

        fields.add(new Field(0,-40, "PARKING",board));

        fields.add(new SubjectField(350,-60, new Point2D(560,310), "INDEKS",board));
        fields.add(new SubjectField(600,-60, new Point2D(810,310), "AM2",board));
        fields.add(new Field(860,-60, "QUIZ",board));
        fields.add(new SubjectField(1120,-60, new Point2D(1330,310), "ALGEBRA",board));
        fields.add(new SubjectField(1380,-60, new Point2D(1590,310), "AM1",board));

        fields.add(new Field(1650,-40, "INNOWACJA",board));

        fields.add(new SubjectField(1730,260, new Point2D(1690,560), "ZAHIR",board));
        fields.add(new Field(1730,520, "MYSTERY BOX",board));
        fields.add(new SubjectField(1730,780, new Point2D(1690,1080),"PP1",board));
        fields.add(new SubjectField(1730,1040, new Point2D(1690,1340), "SO2",board));
        fields.add(new SubjectField(1730,1300, new Point2D(1690,1600), "PP2",board));
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
