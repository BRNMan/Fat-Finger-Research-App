package com.example.fatfinger;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
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
import android.widget.Magnifier;

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

    int lensNumber = 0;

    Magnifier magnifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get input argument telling us which lens to use.
        Bundle b = getIntent().getExtras();
        if(b != null) {
            lensNumber = b.getInt("lensNum");
        }
        Log.println(Log.INFO, "LENSNUM", String.valueOf(lensNumber));

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Empty because we don't want the back button to do anything.
                // The unwavering march of time proceeds forward.

            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

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
                    targetNodeIndex = g.generateGraph(SEED, 50, 12, 10);
                    drawGraph(g);

                    //If it's the second trial, use a Magnifier
                    magnifier = new Magnifier(mImageView);
                }
            }
        });

        startTime = System.currentTimeMillis();
    }

    private OnTouchListener pressListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent me) {
            v.performClick();

            //Use magnifying glass.
            if(lensNumber == 1) {
                if(me.getActionMasked() == MotionEvent.ACTION_DOWN || me.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    magnifier.show(me.getX(), me.getY());
                    //Redraw the graph and set a crosshairs where your finger is.
                    mCanvas.drawColor(mColorBackground);
                    drawGraph(g);
                    mPaint.setColor(mColorText);
                    mCanvas.drawCircle(me.getX(), me.getY(), 3, mPaint);
                    mImageView.invalidate();
                }
            }

            // Lift off tap
            if(me.getActionMasked() == MotionEvent.ACTION_UP) {
                if(lensNumber == 1) {
                    magnifier.dismiss();
                }
                //We should probably test to find the best seeds and densities using some kind of
                //button or scroll bar to go through each possible graph.
                mPaint.setColor(mColorAccent);
                mCanvas.drawCircle(me.getX(), me.getY(), 10.0f, mPaint);

                checkNodeClicked(me);
                //Restart timer.
                startTime = System.currentTimeMillis();

                //Next trial
                trial++;
                if(trial == 21) {
                    changeLens();
                } else {
                    // Make the graph g have different densities for each 1/3 of trials
                    if(trial/7 == 0) {
                        targetNodeIndex = g.generateGraph(SEED + trial, 50, 12, 10);
                    } else if (trial/7 == 1) {
                        targetNodeIndex = g.generateGraph(SEED + trial, 50, 12, 15);
                    } else {
                        targetNodeIndex = g.generateGraph(SEED + trial, 30, 40, 50);
                    }

                    //Clear screen
                    mCanvas.drawColor(mColorBackground);
                    //Start a new graph for the next trial.
                    drawGraph(g);
                    mImageView.invalidate();
                }
            }
            return true;
        }
    };

    private void changeLens() {
        //Start second instructions activity.
        Intent intent = new Intent().putExtra("lensNum", lensNumber + 1);
        if(lensNumber == 0) {
            Log.println(Log.INFO, "Phase 1 of the experiment is over.", "Hooray!");
            intent.putExtra("messageText", "This trial will use a magnifying lens that will activate when you hold down the mouse button. When you lift your finger off, you will select the dot.");

        } else if(lensNumber == 1) {
            Log.println(Log.INFO, "Phase 2 of the experiment is over.", "Hooray!");
            //Start third instructions activity.
            intent.putExtra("messageText", "This trial will use a bubble cursor.");
        } else {
            Log.println(Log.INFO, "Phase 3 of the experiment is over.", "Hooray!");
            intent.putExtra("messageText", "You're done!");
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void checkNodeClicked(MotionEvent me) {
        // Check if the user has clicked on a node.
        // We need to handle clicks ourselves for this.
        ArrayList<Node> nodeList = g.getNodes();

        Node targetNode = nodeList.get(targetNodeIndex);

        //Get minimum distance Node
        Node minNode = nodeList.get(0);
        double minDistance = getDistance(minNode, me.getX(), me.getY());
        for(Node n : nodeList) {
            double distance = getDistance(n, me.getX(), me.getY());
            if(distance < minDistance) {
                minNode = n;
                minDistance = distance;
            }
        }

        //Log if a node was clicked, and if it was correct.
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
    }

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
