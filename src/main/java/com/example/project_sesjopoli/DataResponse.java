package com.example.project_sesjopoli;

public class DataResponse {
    public int playerID;
    public int fieldNumber;

    public DataResponse(int playerID, int fieldNumber) {
        this.playerID = playerID;
        this.fieldNumber = fieldNumber;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }
}