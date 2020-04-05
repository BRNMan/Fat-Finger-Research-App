package com.example.fatfinger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    //Used to actually draw stuff
    private Canvas mCanvas;
    //Used to set the color
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
    //Links the canvas to the ImageView.
    private Bitmap mBitmap;
    private ImageView mImageView;

    private int mColorBackground;
    private int mColorAccent;
    private int mColorOffNode, mColorOnNode;
    private int mColorText;

    private Graph g;
    private int targetNodeIndex;

    private Rect mBounds = new Rect();

    boolean isCanvasInit = false;

    long startTime;
    final int SEED = 2020;
    int trial = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //You can set color values in the colors.xml resource file.
        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);
        mColorOffNode = ResourcesCompat.getColor(getResources(),
                R.color.colorOffNode, null);
        mColorOnNode = ResourcesCompat.getColor(getResources(),
                R.color.colorOnNode, null);
        mColorText = ResourcesCompat.getColor(getResources(),
                R.color.colorText, null);

        mPaint.setColor(mColorBackground);

        mPaintText.setColor(0x00BBBB);
        mPaintText.setTextSize(70);

        //This is where we really work on displaying the graph
        mImageView = findViewById(R.id.imageView);
        mImageView.setOnTouchListener(pressListener);

        //We need this code to run after the image view is initialized so it has a size to pass to the bitmap
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                //Initializes everything the first time.
                if(!isCanvasInit) {
                    mBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(), Bitmap.Config.ARGB_8888);
                    //This is how we associate what we're drawing with the ImageView
                    mImageView.setImageBitmap(mBitmap);
                    mCanvas = new Canvas(mBitmap);
                    //Draw a nice background
                    mCanvas.drawColor(mColorBackground);
                    isCanvasInit = true;


                    //We should probably test to find the best seeds and densities using some kind of
                    //button or scroll bar to go through each possible graph.
                    g = new Graph();
                    targetNodeIndex = g.generateOvalGraph(SEED, 50, 12);
                    drawGraph(g);
                }
            }
        });

        startTime = System.currentTimeMillis();
    }

    private OnTouchListener pressListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent me) {
            v.performClick();

            // Lift off tap
            if(me.getActionMasked() == MotionEvent.ACTION_UP) {
                //We should probably test to find the best seeds and densities using some kind of
                //button or scroll bar to go through each possible graph.
                mPaint.setColor(mColorAccent);
                mCanvas.drawCircle(me.getX(), me.getY(), 10.0f, mPaint);
//                String text = "Hello World";
//                mPaintText.setColor(mColorText);
//                mPaintText.getTextBounds(text, 0, text.length(), mBounds);
//                mCanvas.drawText(text, 100, 100, mPaintText);

                // Check if the user has clicked on a node.
                // We need to handle clicks ourselves for this.
                ArrayList<Node> nodeList = g.getNodes();

                Node targetNode = nodeList.get(targetNodeIndex);


                Node minNode = nodeList.get(0);
                double minDistance = getDistance(minNode, me.getX(), me.getY());
                for(Node n : nodeList) {
                    double distance = getDistance(n, me.getX(), me.getY());
                    if(distance < minDistance) {
                        minNode = n;
                        minDistance = distance;
                    }
                }

                if(minDistance < Node.getSize()*2) {
                    minNode.setOn(true);
                    if(minNode.getX() == targetNode.getX() && minNode.getY() == targetNode.getY()) {
                        Log.println(Log.INFO, "Target Clicked", "got it");
                    } else {
                        Log.println(Log.INFO, "Other Node Clicked", "Target Location(x,y): (" + targetNode.getX() + "," + targetNode.getY() + ") "
                                + "Other Node Location(x,y): (" + minNode.getX() + "," + minNode.getY() + ")");
                    }
                } else {
                    Log.println(Log.INFO, "Background Clicked", "you missed" + minDistance);
                }
                Log.println(Log.INFO, "Click Data", "X: " + me.getX() +  " Y: " + me.getY()
                + "  Target Location(x,y): (" + targetNode.getX() + "," + targetNode.getY() + ")" + " Time taken(ms): " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();


                trial++;
                if(trial >= 10) {
                    Log.println(Log.INFO, "Phase 1 of the experiment is over.", "Hooray!");
                } else {
                    //Clear screen
                    mCanvas.drawColor(mColorBackground);
                    //Start a new graph for the next trial.
                    targetNodeIndex = g.generateOvalGraph(SEED + trial, 50, 12);
                    drawGraph(g);

                    mImageView.invalidate();
                }
            }
            return true;
        }
    };

    private double getDistance(Node n, double x, double y) {
        if(n==null) {
            return -1;
        } else {
            return Math.abs(Math.sqrt(Math.pow(n.getX() - x, 2) + Math.pow(n.getY() - y, 2)));
        }
    }

    private void drawGraph(Graph g) {
        ArrayList<Node> nodes = g.getNodes();

        for(Node node : nodes) {
            // Log.println(Log.INFO, "HELLO", node.getX() + "    " + node.getY() + "   " + Node.getSize());
            if(node.isOn()) {
                mPaint.setColor(mColorOnNode);
            } else {
                mPaint.setColor(mColorOffNode);
            }
            mCanvas.drawCircle((float)node.getX(), (float)node.getY(), (float)Node.getSize(), mPaint);
        }
    }
}
