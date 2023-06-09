package com.example.project_sesjopoli;

import java.util.ArrayList;

public class GameState {
    public ArrayList<Integer> playerPositions;
    public ArrayList<Integer> money;
    public ArrayList<Integer> positionOwners;
    public ArrayList<Integer> priceFields;
    public PunishmentInfo punishmentInfo;
    public ArrayList<String> names;
    public ArrayList<Boolean> stopFieldFlags;
    public ArrayList<Boolean> playerLostFlags;
    public int whoseTurn;
    public int playerId;
    public ArrayList<Question> questions;
}
