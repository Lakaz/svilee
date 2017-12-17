package com.example.Laki.myapplication.backend;

/**
 * Created by Laki on 26/11/2017.
 */

public class ItemStatusResponse {

    private int isTaken;
    private String takerName;
    private String takerUUID;
    private String timeTaken;

    public int isTaken() {
        return isTaken;
    }

    public void setTaken(int taken) {
        isTaken = taken;
    }

    public String getTakerName() {
        return takerName;
    }

    public void setTakerName(String takerName) {
        this.takerName = takerName;
    }

    public String getTakerUUID() {
        return takerUUID;
    }

    public void setTakerUUID(String takerUUID) {
        this.takerUUID = takerUUID;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }
}
