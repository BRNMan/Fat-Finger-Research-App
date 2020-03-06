package com.example.fatfinger;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Random;

class Graph {
    //Ideally, this should be sorted by x, or distance from upper left corner maybe.
    //Maybe I should make it a BinaryTree/Heap.
    private ArrayList<Node> nodeList;

    Graph(int seed, int density) {
        nodeList = new ArrayList<>();
        generateGraph(seed, density);
    }

    private void generateGraph(int seed, int density) {
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
            Node n = new Node(generator.nextFloat()*screenWidth, generator.nextFloat()*screenHeight, false);
            nodeList.add(n);
        }
    }

    ArrayList<Node> getNodes() {
        return nodeList;
    }
}
