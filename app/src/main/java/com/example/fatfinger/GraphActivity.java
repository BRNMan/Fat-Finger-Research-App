package com.example.fatfinger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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

    private Rect mBounds = new Rect();

    boolean isCanvasInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //You can set color values in the colors.xml resource file.
        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null);
        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);
        mColorOffNode = ResourcesCompat.getColor(getResources(),
                R.color.colorOffNode, null);
        mColorOnNode = ResourcesCompat.getColor(getResources(),
                R.color.colorOnNode, null);

        mPaint.setColor(mColorBackground);

        mPaintText.setColor(0x00BBBB);
        mPaintText.setTextSize(70);

        //This is where we really work on displaying the graph
        mImageView = findViewById(R.id.imageView);
        mImageView.setOnTouchListener(pressListener);

    }

    private OnTouchListener pressListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent me) {
            v.performClick();
            int vWidth = v.getWidth();
            int vHeight = v.getHeight();
            int halfWidth = vWidth/2;
            int halfHeight =  vHeight/2;

            Graph g = new Graph(10069, 200);

            //Initializes everything the first time.
            if(!isCanvasInit) {
                mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
                //This is how we associate what we're drawing with the ImageView
                mImageView.setImageBitmap(mBitmap);
                mCanvas = new Canvas(mBitmap);
                //Draw a nice background
                mCanvas.drawColor(mColorBackground);
                isCanvasInit = true;

                //We should probably test to find the best seeds and densities using some kind of
                //button or scroll bar to go through each possible graph.
                drawGraph(g);
            }


            if(me.getActionMasked() == MotionEvent.ACTION_UP) {
                //We should probably test to find the best seeds and densities using some kind of
                //button or scroll bar to go through each possible graph.
                drawGraph(g);
                mPaint.setColor(mColorAccent);
                mCanvas.drawCircle(me.getX(), me.getY(), 10.0f, mPaint);
                String text = "Hello World";
                mPaintText.setColor(mColorAccent);
                mPaintText.getTextBounds(text, 0, text.length(), mBounds);
                mCanvas.drawText(text, 100, 100, mPaintText);
                mImageView.invalidate();
            }
            return true;
        }
    };

    private void drawGraph(Graph g) {
        ArrayList<Node> nodes = g.getNodes();

        for(Node node : nodes) {
            Log.println(Log.INFO, "HELLO", node.getX() + "    " + node.getY() + "   " + Node.getSize());
            if(node.isOn()) {
                mPaint.setColor(mColorOnNode);
            } else {
                mPaint.setColor(mColorOffNode);
            }
            mCanvas.drawCircle(node.getX(), node.getY(), Node.getSize(), mPaint);
        }
    }
}
