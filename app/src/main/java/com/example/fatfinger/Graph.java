package com.example.fatfinger;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

class Graph {
    //Ideally, this should be sorted by x, or distance from upper left corner maybe.
    //Maybe I should make it a BinaryTree/Heap.
    private ArrayList<Node> nodeList;
    private Node targetNode;

    Graph() {
        nodeList = new ArrayList<>();
    }

    //Number of points must be greater than 5.
    //Size must be greater than 7.
    public int generateGraph(int seed, int numberOfPoints, double size) {
        nodeList.clear();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        Random generator = new Random(seed);
        //Sequential seeds produce non random numbers, so run through a few times to increase randomness.
        for(int i = 0; i < 20; i++) {
            generator.nextDouble();
        }

        double xOffset = 0.15;
        double leftX = screenWidth*xOffset;

        double yOffset = 0.3;
        double topY = screenHeight*yOffset;
        //Generates first five points
        double x,y;
        x = generator.nextDouble()*screenWidth*(1-2*xOffset) + leftX;
        y = generator.nextDouble()*screenHeight*(1-2*yOffset) + topY;

        nodeList.add(new Node(x, y, true, size));
        nodeList.add(new Node(x - 2*size, y, false, size));
        nodeList.add(new Node(x + 2*size, y, false, size));
        nodeList.add(new Node(x, y - 2*size, false, size));
        nodeList.add(new Node(x, y + 2*size, false, size));

        double centerX = x, centerY = y;

        //Makes rest of points randomly dispersed away from first five points.
        for(int i = 5; i < numberOfPoints; i++) {
            x = generator.nextDouble()*screenWidth*(1-2*xOffset) + leftX;
            y = generator.nextDouble()*screenHeight*(1-2*yOffset) + topY;
            //Generate a point that's outside of the box formed by the initial points.
            while(x > centerX - 3*size && x < centerX + 3*size
            && y > centerY - 3*size && y < centerY + 3*size) {
                x = generator.nextDouble()*screenWidth*(1-2*xOffset) + leftX;
                y = generator.nextDouble()*screenHeight*(1-2*yOffset) + topY;
            }

            //Generate random size between 7 and size
            double randSize = generator.nextDouble() * (size - 7);
            if(randSize < 0) {
                randSize *= -1;
            }
            randSize += 8;

            nodeList.add(new Node(x, y, false, randSize));
        }

        return 0;
    }

    ArrayList<Node> getNodes() {
        return nodeList;
    }

    Node getTargetNode() { return targetNode; }

    void setNodes(ArrayList<Node> nodes) {
        this.nodeList = nodes;
    }
}
