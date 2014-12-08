package edu.uark.csce.tilebreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lukebrown on 11/29/14.
 */
public class Block {
    double lx,rx,by,ty,width,height;
    public int health,color;
    int[] healthColors;
    int[] healthOutLineColors;
    public Block(int lx, int rx, int ty, int by, int health){
        this.lx = lx;
        this.rx = rx;
        this.ty = ty;
        this.by = by;
        this.width = rx-lx;
        this.height = by-ty;
        this.health = health;
        healthColors = new int[]{Color.GREEN, Color.YELLOW, Color.RED ,Color.BLACK};
        healthOutLineColors = new int[]{Color.rgb(0,128,0),Color.rgb(128,128,0),Color.rgb(128,0,0),Color.rgb(100,100,100)};
        color = healthColors[health-1];
    }

    public void paint(Paint paint, Canvas canvas){
        color = healthColors[health - 1];
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect((int)Math.round(lx), (int)Math.round(ty), (int)Math.round(rx), (int)Math.round(by), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(healthOutLineColors[health - 1]);
        paint.setStrokeWidth(3.0f);
        canvas.drawRect((int)Math.round(lx)+1, (int)Math.round(ty)+1, (int)Math.round(rx)-1, (int)Math.round(by)-1, paint);
    }

    public void update() {
        this.ty+=0.3;
        this.by+=0.3;
    }

    public void hit(){
        this.health--;
    }

    public int getHealth(){
        return this.health;
    }
}
