package com.example.project_sesjopoli.game_objects;

import java.util.Random;

public class SubjectField extends Field{

    public static final int PUNISHMENT_BONUS = 2;
    private SubjectFieldType type;
    private int price;
    private Pawn owner;

    private int punishmentPrice;




    public SubjectField(int x, int y, String name,SubjectFieldType type, int price, int punishmentPrice, Board board) {
        super(x, y, name, board);
        this.type = type;
        this.price=price;
        this.punishmentPrice=punishmentPrice;
    }

    public SubjectField(int x, int y, String name,SubjectFieldType type, Board board) { // losowe pola
        super(x, y, name, board);
        this.type = type;
        this.price=new Random().nextInt(6) + 1;;
        this.punishmentPrice=new Random().nextInt(6) + 1;
    }

    public void buyField(Pawn pawn){
        if(pawn.getEctsPoints() < price)
            return;

        pawn.getBoughtFields().add(this);
        pawn.setEctsPoints(pawn.getEctsPoints()-price);
        owner=pawn;
    }

    public void fieldEntryFee(Pawn owner, Pawn stranger){

        // todo co jak nie ma pieniedzy(przegrywa)

        int punishment;
        if(hasAllFieldsOfOneType(owner))
            punishment=punishmentPrice* PUNISHMENT_BONUS;
        else
            punishment=punishmentPrice;

        //restaurant

        stranger.setEctsPoints(stranger.getEctsPoints()-punishment);
        owner.setEctsPoints(owner.getEctsPoints()+punishment);

    }

    public boolean hasAllFieldsOfOneType(Pawn owner){
        int counter=0;
        for(SubjectField field : owner.getBoughtFields()){
            if(field.type == type)
                counter++;
        }

        // jesli ma wszystkie pola danego koloru => bonus
        return counter == this.board.getSubjectFieldTypeAppearances().get(type);
    }

    public Pawn getOwner(){
        return owner;
    }
}
