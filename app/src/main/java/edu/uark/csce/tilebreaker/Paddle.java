package edu.uark.csce.tilebreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lukebrown on 11/29/14.
 */
public class Paddle {
    int x,y,lx,rx,by,ty,width,height,screenWidth,screenHeight;
    //Color is an int
    int color;

    public Paddle(int x,int y,int width,int height,int screenWidth,int screenHeight){
        this.color = Color.rgb(30,30,30);
        this.x = x;
        this.y = y;
        this.lx = x-width/2;
        this.rx = x+width/2;
        this.ty = y-height/2;
        this.by = y+height/2;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    //Sets paddle to touch x, unless the paddle would move off the screen
    public void update(int touchX){
        if(touchX <= width/2){
            x = width/2;
        }else if(touchX >= screenWidth-width/2){
            x = screenWidth-width/2;
        }else{
            x = touchX;
        }
        lx = x-width/2;
        rx = x+width/2;
        ty = y-height/2;
        by = y+height/2;
    }

    //Draws a filled rect where the paddle is.
    public void paint(Paint paint, Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect(lx, ty, rx, by, paint);
    }
}
