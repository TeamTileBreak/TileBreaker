package edu.uark.csce.tilebreaker;

import edu.uark.csce.tilebreaker.util.PauseDialogFragment;
import edu.uark.csce.tilebreaker.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.support.v4.app.FragmentActivity;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TileBreakerActivity extends FragmentActivity {

    public static boolean paused = false;
    public static boolean upgrade = false;
    private MyView view;
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
                if (touchX < 250 && touchY < 100) {
                    if (!paused) {
                        pauseGame(v);
                        paused = true;
                        //Log.d("Pause", "Calling pauseGame from TileBreakerActivity");
                    }
                    System.out.println(touchY);
                } else if (touchX > v.getWidth() - 250 && touchY < 100) {
                    if (!upgrade) {
                        chooseUpgrade(v);
                        upgrade = true;
                        //Log.d("Upgrade", "Calling chooseUpgrade from TileBreakerActivity");
                    }
                } else {
                    onScreenTouch(touchX, touchY);
                }

                return true;
            }
        });
    }

    public class MyView extends View {
        int x;
        int y;
        int paddleX;
        int paddleY;
        int paddleWidth = 200;
        int ballYVel = 6;
        int ballXVel = 6;
        boolean goingLeft = false;
        boolean goingUp = false;
        boolean alive = true;
        boolean firstTime = true;
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            if(!paused)
                updateBall();

            if(firstTime){
                init();
            }

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            drawBall(paint,canvas);
            drawPaddle(paint,canvas);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#000000"));
            paint.setStrokeWidth(5);
            canvas.drawRect(0,0,250,100,paint);
            canvas.drawRect(getWidth()-250,0,getWidth(),100,paint);
            paint.setStrokeWidth(0);
            paint.setTextSize(60);
            canvas.drawText("Pause",40,75,paint);
            canvas.drawText("Upgrade",getWidth()-235,75,paint);
            invalidate();
        }

        private void init(){
            paddleX = getWidth()/2;
            paddleY = getHeight()-150;
            firstTime = false;
            x=1;
            y=1;
            ballXVel = 6;
            ballYVel = 6;
            goingLeft = false;
            goingUp = false;
        }

        private void die(){
            firstTime = true;
        }

        private void updateBall(){
            if(x>=getWidth() || x<=0){
                ballXVel = -ballXVel;
            }
            if(y>=getHeight()-150){
                if(x >= paddleX-paddleWidth/2 && x <= paddleX+paddleWidth/2){
                    ballXVel = (x-paddleX)* 6 / (paddleWidth/2);
                    ballYVel = -ballYVel;
                }else{
                    die();
                }
            }else if(y<=0){
                ballYVel = -ballYVel;
            }
            x+=ballXVel;
            y+=ballYVel;
        }

        private void onTouch(int touchX, int touchY){
            updatePaddle(touchX);
        }

        private void updatePaddle(int touchX){
            if(touchX <= paddleWidth/2){
                paddleX = paddleWidth/2;
            }else if(touchX >= getWidth()-paddleWidth/2){
                paddleX = getWidth()-paddleWidth/2;
            }else{
                paddleX = touchX;
            }
        }



        private void drawBall(Paint paint, Canvas canvas){
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#AAAAAA"));
            canvas.drawCircle(x, y, 10, paint);
        }

        private void drawPaddle(Paint paint, Canvas canvas){
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#AAAAAA"));
            canvas.drawRect(paddleX - paddleWidth / 2, paddleY+10, paddleX + paddleWidth / 2, paddleY + 20, paint);
        }
    }

    public void onScreenTouch(int touchX,int touchY){
        this.view.onTouch(touchX,touchY);
    }

    public void pauseGame(View view) {
        PauseDialogFragment df;
        df = new PauseDialogFragment(this);
        df.show(getSupportFragmentManager(), "fragment_alert");
    }

    public void chooseUpgrade(View view) {
        Intent intent = new Intent(this, UpgradeActivity.class);
        TileBreakerActivity.this.finish();
        startActivity(intent);
    }

}
