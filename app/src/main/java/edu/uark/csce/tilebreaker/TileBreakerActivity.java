package edu.uark.csce.tilebreaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import edu.uark.csce.tilebreaker.util.PauseDialogFragment;
import edu.uark.csce.tilebreaker.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TileBreakerActivity extends FragmentActivity implements SensorEventListener {

    public static boolean paused = false;
    public static boolean upgrade = false;
    private MyView view;
    public static int x;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences(UpgradeActivity.PREF_NAME,MODE_PRIVATE);
        boolean Lightening = pref.getBoolean("Lightening",false);
        Log.i("Lightening", String.valueOf(Lightening));

        view = new MyView(this);
        setContentView(view);
        paused = false;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                //Log.d("TOUCH!", "Touched at (" + touchX + "," + touchY + ")");
                if (touchX < view.getWidth() && touchY < 100) {
                    if (!paused) {
                        pauseGame(v);
                        paused = true;
                    }
                }else{
                    onScreenTouch(touchX, touchY);
                }

                return true;
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public class MyView extends View {
        //Custom Objects
        Paddle paddle;
        Ball ball;
        ArrayList<Block> blocks;

        //Default Graphics
        Paint paint;

        //Technical vars
        int screenWidth,screenHeight;

        //Game logic vars
        boolean alive = false;


        //Constructor for MyView, called before any code within
        public MyView(Context context) {
            super(context);
            paint = new Paint();

            //Getting Screen Height and Width
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;

            //initialize screen
            spawn();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(!paused) {
                alive = ball.update(paddle,blocks);
                for(Block block : blocks){
                    block.update();
                }
            }
            if(!alive)
                spawn();

            //clearing canvas by filling with a color
            canvas.drawColor(Color.rgb(0,153,204));

            //drawing objects
            ball.paint(paint,canvas);
            paddle.paint(paint,canvas);
            for(Block block : blocks){
                block.paint(paint,canvas);
            }

            //Drawing Pause Button
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(5);
            canvas.drawRect(0,0,getWidth(),100,paint);
            paint.setStrokeWidth(0);
            paint.setTextSize(60);

            //Tells view that it needs to be updated
            int pauseWidth = (getWidth()/2)-80;
            canvas.drawText("Pause",pauseWidth,75,paint);

            // Updates paddle via accelerometer
            if(!paused) {
                onResume();
                paddle.update(x);
            }

            invalidate();
        }

        private void spawn(){
            //creates paddle with an x,y,width, and height
            paddle = new Paddle(screenWidth/2,screenHeight-150,200,10,screenWidth,screenHeight);
            //creates ball with an x and a y
            ball = new Ball(paddle.x,paddle.ty,screenWidth,screenHeight);

            //Creates array of blocks
            blocks = newBlocks(10,5);
            alive = true;
        }

        private ArrayList<Block> newBlocks(int rows, int rowWidth) {
            ArrayList<Block> retList = new ArrayList<Block>();
            for(int i = 1; i <= rows; i++){
                for(int j = 0; j < rowWidth; j++){
                    retList.add(new Block(screenWidth/(rowWidth)*j,screenWidth/(rowWidth)*(j+1),-30*(i+1),-30*i,4));
                }
            }
            return retList;
        }

        private void onTouch(int touchX, int touchY){
            paddle.update(touchX);
        }


    }

    public void onScreenTouch(int touchX,int touchY){
        this.view.onTouch(touchX,touchY);
    }

    public void pauseGame(View view) {
        PauseDialogFragment df;
        df = new PauseDialogFragment(this);
        df.show(getSupportFragmentManager(), "fragment_alert");

        // Stops listening to accelerometer
        onPause();
    }

    public void chooseUpgrade(View view) {
        Intent intent = new Intent(this, UpgradeActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }



    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            x -= (int) event.values[0] * 8;

            if (x < 0) {
                x = 0;
            } else if (x > view.screenWidth) {
                x = view.screenWidth;
            }

        }
    }

}
