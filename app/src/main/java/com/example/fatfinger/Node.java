package com.example.fatfinger;

import androidx.annotation.Nullable;

public class Node implements Comparable<Node>{
    private double x, y;
    private static double size;
    private boolean isOn;

    Node(double X, double Y, boolean isOn) {
        this.x = X;
        this.y = Y;
        this.isOn = isOn;
        size = 15.0f;
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

    static double getSize() {
        return size;
    }

    public static void setSize(double size) {
        Node.size = size;
    }

    @Override
    public int compareTo(Node o) {
        int returnVal = Double.compare(this.y, o.y);
        if(returnVal == 0) {
            returnVal = Double.compare(this.x, o.x);
        }

        return returnVal;
    }
}
