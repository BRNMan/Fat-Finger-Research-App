package com.example.fatfinger;

public class DataRow {
    public DataRow(int participant_id, int lens_No, int trial_No, boolean target_Clicked, double click_X, double click_Y, double target_X, double target_Y, long time_Taken) {
        this.participant_id = participant_id;
        Lens_No = lens_No;
        Trial_No = trial_No;
        Target_Clicked = target_Clicked;
        Click_X = click_X;
        Click_Y = click_Y;
        Target_X = target_X;
        Target_Y = target_Y;
        Time_Taken = time_Taken;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    public int getLens_No() {
        return Lens_No;
    }

    public void setLens_No(int lens_No) {
        Lens_No = lens_No;
    }

    public int getTrial_No() {
        return Trial_No;
    }

    public void setTrial_No(int trial_No) {
        Trial_No = trial_No;
    }

    public boolean isTarget_Clicked() {
        return Target_Clicked;
    }

    public void setTarget_Clicked(boolean target_Clicked) {
        Target_Clicked = target_Clicked;
    }

    public double getClick_X() {
        return Click_X;
    }

    public void setClick_X(double click_X) {
        Click_X = click_X;
    }

    public double getClick_Y() {
        return Click_Y;
    }

    public void setClick_Y(double click_Y) {
        Click_Y = click_Y;
    }

    public double getTarget_X() {
        return Target_X;
    }

    public void setTarget_X(double target_X) {
        Target_X = target_X;
    }

    public double getTarget_Y() {
        return Target_Y;
    }

    public void setTarget_Y(double target_Y) {
        Target_Y = target_Y;
    }

    public long getTime_Taken() {
        return Time_Taken;
    }

    public void setTime_Taken(long time_Taken) {
        Time_Taken = time_Taken;
    }

    int participant_id, Lens_No, Trial_No;
    boolean Target_Clicked;
    double Click_X, Click_Y, Target_X, Target_Y;
    long Time_Taken;
}
