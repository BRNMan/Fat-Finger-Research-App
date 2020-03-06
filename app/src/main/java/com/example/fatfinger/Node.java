package com.example.fatfinger;

public class Node {
    private float x, y;
    private static float size;
    private boolean isOn;

    Node(float X, float Y, boolean isOn) {
        this.x = X;
        this.y = Y;
        this.isOn = isOn;
        size = 15.0f;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    boolean isOn() {
        return isOn;
    }

    static float getSize() {
        return size;
    }

    public static void setSize(float size) {
        Node.size = size;
    }
}
