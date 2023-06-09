package com.example.project_sesjopoli;

public class PunishmentInfo {
    public int payerId;
    public int payeeId;
    public int cost;

    public PunishmentInfo(int payerId, int payeeId, int cost){
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.cost = cost;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isClear(){
        return this.payerId == -1 && this.payeeId == -1&& this.cost == -1;
    }
}
