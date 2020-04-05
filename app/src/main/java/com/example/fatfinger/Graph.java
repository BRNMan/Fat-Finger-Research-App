package com.example.fatfinger;

import android.content.res.Resources;
import android.security.ConfirmationNotAvailableException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Graph {
    //Ideally, this should be sorted by x, or distance from upper left corner maybe.
    //Maybe I should make it a BinaryTree/Heap.
    private ArrayList<Node> nodeList;
    private Node targetNode;

    Graph() {
        nodeList = new ArrayList<>();
    }

    public void generateRandomGraph(int seed, int density) {
        nodeList.clear();
        //Ideally, we want to put more points on bigger screens, and keep the node size the same in inches.
        //I'm going to worry about that later. For now, assume the same screen size.
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        //int screenArea = screenWidth*screenHeight;

        //Give seed to random number generator we will be using for consistency.
        //This Random class must return the same numbers every time for a given seed.
        Random generator = new Random(seed);

        //Scale proportionally to density and area and make random points in x and y axes.
        for(int i = 0; i < density; i++) {
            float nodeX, nodeY;
            Node n = new Node(generator.nextDouble()*screenWidth, generator.nextDouble()*screenHeight, false);
            nodeList.add(n);
        }
    }


    public void generateEvenlySpacedGraph(int density) {
        nodeList.clear();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        float aspectRatio = screenHeight/screenWidth;
        Node.setSize(15);
        for(int y = 0; y < screenHeight; y += (int)(aspectRatio * screenHeight/density)) {
            for(int x = 0; x < screenWidth; x += (int)((1/aspectRatio) * screenHeight/density)) {
                Node n = new Node(x, y, false);
                nodeList.add(n);
            }
        }
    }


    public int generateOvalGraph(int seed, int numberOfClusters, int overlapPixels) {
        nodeList.clear();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        Random generator = new Random(seed);
        int targetIndex = generator.nextInt() % (3*numberOfClusters);
        //Because % leaves in negative numbers and we cant have a negative index.
        if(targetIndex < 0) {
            targetIndex *= -1;
        }

        double xOffset = 0.15;
        double leftX = screenWidth*xOffset;

        double yOffset = 0.3;
        double topY = screenHeight*yOffset;

        Node.setSize(15);
        //Generates three nodes at a time
        for(int i = 0; i < numberOfClusters; i++) {
            Node n1, n2, n3;
            double x, y;
            x = generator.nextDouble()*screenWidth*(1-2*xOffset) + leftX;
            y = generator.nextDouble()*screenHeight*(1-2*yOffset) + topY;
            boolean isN1On = false, isN2On = false, isN3On = false;

            if(i == targetIndex/3) {
                int onNode = targetIndex %3;
                switch(onNode) {
                    case 0:
                        isN1On = true;
                        break;
                    case 1:
                        isN2On = true;
                        break;
                    case 2:
                    default:
                        isN3On = true;
                }

            }

            //Make a triangle of nodes
            n1 = new Node(x, y, isN1On);
            n2 = new Node(x + overlapPixels, y, isN2On);
            n3 = new Node(x + overlapPixels/2, y - overlapPixels, isN3On);
            nodeList.add(n1);
            nodeList.add(n2);
            nodeList.add(n3);
        }

        return targetIndex;
    }

    ArrayList<Node> getNodes() {
        return nodeList;
    }

    Node getTargetNode() { return targetNode; }

    void setNodes(ArrayList<Node> nodes) {
        this.nodeList = nodes;
    }
}
