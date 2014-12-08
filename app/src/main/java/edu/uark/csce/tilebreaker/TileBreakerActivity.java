package edu.uark.csce.tilebreaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.DragEvent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.support.v4.app.FragmentActivity;
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
    public static int score = 0;

    public static boolean shotgunBall = false;
    public static boolean doubleBall = false;
    public static boolean extendedPaddle = false;
    public static boolean net = false;
    public static boolean doubleDamageBall = false;
    public static boolean laserShot = false;
    public static boolean flameThrower = false;
    public static boolean turrets = false;
    public static boolean stickyPaddle = false;
    private MyView view;
    public static int x;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences(UpgradeActivity.PREF_NAME,MODE_PRIVATE);
        doubleBall = false;
        doubleBall = pref.getBoolean("doubleBall", false);
        shotgunBall = false;
        shotgunBall = pref.getBoolean("shotgunBall", false);
        flameThrower = false;
        flameThrower = pref.getBoolean("flameThrower", false);
        extendedPaddle = false;
        extendedPaddle = pref.getBoolean("extendedPaddle", false);
        net = false;
        net = pref.getBoolean("net", false);
        doubleDamageBall = false;
        doubleDamageBall = pref.getBoolean("doubleDamageBall", false);
        laserShot = false;
        laserShot = pref.getBoolean("laserShot", false);
        turrets = false;
        turrets = pref.getBoolean("turrets", false);
        stickyPaddle = false;
        stickyPaddle = pref.getBoolean("UG9", false);

        score = pref.getInt("score",score);

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
                    x = touchX;
                }
                if(event.getAction() == android.view.MotionEvent.ACTION_UP){
                    onScreenRelease();
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
        ArrayList<Ball> balls;
        ArrayList<Block> blocks;
        ArrayList<Ball> flameThrowerShots;
        Ball turretBall;

        //Default Graphics
        Paint paint;

        //Technical vars
        int screenWidth,screenHeight;

        //Game logic vars
        boolean alive = false;
        long flameThrowerTime = 0;


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
                for(int i = 0; i < balls.size(); i++) {
                    Ball ball = balls.get(i);
                    int retval = ball.update(paddle, blocks);
                    if (retval == 1) {
                        if(ball.tempBall){
                            balls.remove(i);
                            i--;
                            score++;
                        }else if(ball.doubleDamage){
                            score+=2;
                        }else{
                            score++;
                        }
                    } else if (retval == -1) {
                        if(net){
                            ball.yvel = -Math.abs(ball.yvel);
                            net = false;
                        }
                        if(ball.y >= screenHeight || ball.tempBall) {
                            balls.remove(i);
                            i--;
                        }
                    // ball hit the paddle
                    } else if (retval == 2){
                        if(ball.tempBall) {
                            balls.remove(i);
                            i--;
                        }else {
                            if(stickyPaddle){
                                ball.yvel = 0;
                                ball.y = paddle.ty-ball.r-1;
                                ball.stickyPaddleOffset = ball.x - paddle.x;
                            }
                            if(laserShot){
                                Ball tempBall1 = new Ball(paddle.lx, paddle.ty, screenWidth, screenHeight);
                                Ball tempBall2 = new Ball(paddle.rx, paddle.ty, screenWidth, screenHeight);
                                tempBall1.xvel = 0;
                                tempBall2.xvel = 0;
                                tempBall1.color = Color.GREEN;
                                tempBall2.color = Color.GREEN;
                                tempBall1.tempBall = true;
                                tempBall2.tempBall = true;
                                balls.add(tempBall1);
                                balls.add(tempBall2);
                            }
                            if (doubleBall && balls.size() < 10) {
                                Ball tempBall = new Ball(paddle.x, paddle.ty, screenWidth, screenHeight);
                                if(doubleDamageBall){
                                    tempBall.doubleDamage = true;
                                }
                                tempBall.xvel = -ball.xvel;
                                balls.add(tempBall);
                            }
                            if (shotgunBall) {
                                Ball tempBall1 = new Ball(paddle.x, paddle.ty, screenWidth, screenHeight);
                                Ball tempBall2 = new Ball(paddle.x, paddle.ty, screenWidth, screenHeight);
                                Ball tempBall3 = new Ball(paddle.x, paddle.ty, screenWidth, screenHeight);
                                Ball tempBall4 = new Ball(paddle.x, paddle.ty, screenWidth, screenHeight);
                                tempBall1.tempBall = true;
                                tempBall2.tempBall = true;
                                tempBall3.tempBall = true;
                                tempBall4.tempBall = true;
                                tempBall1.xvel = -tempBall1.xvel;
                                tempBall2.xvel = -tempBall2.xvel / 2;
                                tempBall3.xvel = tempBall3.xvel / 2;
                                tempBall1.color = Color.LTGRAY;
                                tempBall2.color = Color.LTGRAY;
                                tempBall3.color = Color.LTGRAY;
                                tempBall4.color = Color.LTGRAY;
                                balls.add(tempBall1);
                                balls.add(tempBall2);
                                balls.add(tempBall3);
                                balls.add(tempBall4);
                            }
                            if(flameThrower){
                                flameThrowerTime = System.currentTimeMillis();
                            }
                        }
                    }
                }
                for(int i = 0; i < flameThrowerShots.size(); i++){
                    if(flameThrowerShots.get(i).y < paddle.ty-400){
                        flameThrowerShots.remove(i);
                        i--;
                    }else{
                        int retval = flameThrowerShots.get(i).update(paddle,blocks);
                        if(retval == 1){
                            flameThrowerShots.remove(i);


                            i--;
                        }
                    }
                }
                if(turrets){
                    turretBall.update(paddle,blocks);
                    if(turretBall.y >= paddle.ty-60){
                        turretBall.yvel = -turretBall.yvel;
                        turretBall.y = paddle.ty-60;
                    }
                }
                if(balls.size() == 0){
                    alive = false;
                }
                double smallestY = 100.0;
                for(Block block : blocks){
                    block.update();
                    if(block.by >= paddle.ty){
                        alive = false;
                    }
                    if(block.ty < smallestY){
                        smallestY = block.ty;
                    }
                }
                if(smallestY > 0){
                    for(int i = 0; i < 5; i++){
                        blocks.add(new Block(screenWidth/(5)*i,screenWidth/(5)*(i+1),-30,0,4));
                    }
                }
                if(System.currentTimeMillis()-flameThrowerTime < 1000){
                    Random r = new Random();
                    if(r.nextInt(10)==1) {
                        Ball tempBall = new Ball(r.nextInt(getWidth()), getHeight(), screenWidth, screenHeight);
                        tempBall.color = Color.rgb(255, r.nextInt(128) + 128, 0);
                        tempBall.xvel = 0;
                        flameThrowerShots.add(tempBall);
                    }
                }
            }
            if(!alive) {
                SharedPreferences.Editor pref = getSharedPreferences(UpgradeActivity.PREF_NAME,MODE_PRIVATE).edit();
                pref.putInt("score",score);
                pref.commit();
                chooseUpgrade(this);
            }else {

                //clearing canvas by filling with a color
                canvas.drawColor(Color.rgb(0, 153, 204));


                if(net){
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.CYAN);
                    canvas.drawRect(0,paddle.ty,getWidth(),paddle.by, paint);
                }
                if(laserShot){
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.BLACK);
                    canvas.drawRect(paddle.lx,paddle.ty-15,paddle.lx+10,paddle.ty,paint);
                    canvas.drawRect(paddle.rx-10,paddle.ty-15,paddle.rx,paddle.ty,paint);
                }
                //drawing objects
                for (Ball ball : balls) {
                    ball.paint(paint, canvas);
                }
                for (Ball ball : flameThrowerShots){
                    ball.paint(paint,canvas);
                }
                if(turrets){
                    turretBall.paint(paint,canvas);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.YELLOW);
                    canvas.drawRect(turretBall.x-60,paddle.ty-60,turretBall.x+30,paddle.ty-50,paint);
                }
                paddle.paint(paint, canvas);
                for (Block block : blocks) {
                    block.paint(paint, canvas);
                }

                //Drawing Pause Button
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(5);
                canvas.drawRect(0, 0, getWidth(), 100, paint);
                paint.setStrokeWidth(0);
                paint.setTextSize(60);

                //Tells view that it needs to be updated
                int pauseWidth = (getWidth() / 2) - 80;
                canvas.drawText("Pause", pauseWidth, 75, paint);
                invalidate();

            // Updates paddle via accelerometer
            if(!paused) {
                onResume();
                paddle.update(x);
            }

            invalidate();
            }
        }

        private void spawn(){
            //creates paddle with an x,y,width, and height
            int paddleWidth = 125;
            if(extendedPaddle){
                paddleWidth = 160;
            }
            flameThrowerShots = new ArrayList<Ball>();
            paddle = new Paddle(screenWidth/2,screenHeight-150,paddleWidth,10,screenWidth,screenHeight);
            if(turrets){
                turretBall = new Ball(paddle.x, paddle.ty-30, screenWidth, screenHeight);
                turretBall.xvel = -turretBall.xvel;
                turretBall.color = Color.YELLOW;
            }
            //creates ball with an x and a y
            balls = new ArrayList<Ball>();
            balls.add(new Ball(paddle.x,paddle.ty,screenWidth,screenHeight));
            if(doubleDamageBall){
                balls.get(0).doubleDamage = true;
            }

            //Creates array of blocks
            blocks = newBlocks(20,5);
            alive = true;
        }

        private ArrayList<Block> newBlocks(int rows, int rowWidth) {
            ArrayList<Block> retList = new ArrayList<Block>();
            Random r = new Random();
            for(int i = 1; i <= rows; i++){
                for(int j = 0; j < rowWidth; j++){
                    retList.add(new Block(screenWidth/(rowWidth)*j,screenWidth/(rowWidth)*(j+1),-30*(i+1),-30*i,Math.min((int)(4*((i+0.0)/rows)+1),4)));
                }
            }
            return retList;
        }

        private void onTouch(int touchX, int touchY){
            paddle.update(touchX);
        }

        private void onRelease(){
            for(Ball ball : balls){
                System.out.println("RELEASE");
                if(ball.yvel == 0){
                    ball.yvel = -9.0;
                }
            }
        }


    }

    public void onScreenTouch(int touchX,int touchY){
        this.view.onTouch(touchX,touchY);
    }

    public void onScreenRelease(){
        this.view.onRelease();
    }

    public void pauseGame(View view) {
        PauseDialogFragment df;
        df = new PauseDialogFragment(this);
        df.score = score;
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
