package com.example.fatfinger;

import android.content.res.Resources;

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

    public int generateGraph(int seed, int numberOfClusters, double overlapPixels, double size ) {
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
        Node.setSize(size);
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
