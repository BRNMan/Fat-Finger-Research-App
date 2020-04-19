package com.example.fatfinger;

import android.util.Log;

import androidx.annotation.Nullable;

public class Node implements Comparable<Node>{
    private double x, y;
    private double size;
    private boolean isOn;

    Node(double X, double Y, boolean isOn, double size) {
        this.x = X;
        this.y = Y;
        this.isOn = isOn;
        this.size = size;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    boolean isOn() {
        return isOn;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) { this.size = size; }

    @Override
    public int compareTo(Node o) {
        int returnVal = Double.compare(this.y, o.y);
        if(returnVal == 0) {
            returnVal = Double.compare(this.x, o.x);
        }

        return returnVal;
    }
}
