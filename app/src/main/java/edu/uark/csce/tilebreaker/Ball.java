package edu.uark.csce.tilebreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by lukebrown on 11/29/14.
 */
public class Ball {
    int x,y,screenWidth,screenHeight;
    double xvel,yvel;
    int r = 10;
    //Color is an int
    int color;

    boolean tempBall = false;
    boolean doubleDamage = false;
    int stickyPaddleOffset = 0;

    public Ball(int x,int paddleTY,int screenWidth,int screenHeight){
        color = Color.BLACK;
        this.x = x;
        this.y = paddleTY-this.r-1;
        this.xvel = 9;
        this.yvel = -9;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    //Returns 1 if the ball hit a block, 0 if nothing happened, -1 if it died
    public int update(Paddle paddle,ArrayList<Block> blocks){

        boolean collidedWithBlock = checkBlockCollisions(blocks);
        //Ball is hitting a side
        if(x >= screenWidth-r){
            this.xvel = -Math.abs(xvel);
            this.x = screenWidth-r-1;
        }else if(x <= r){
            this.xvel = Math.abs(xvel);
            this.x = r+1;
        }
        //Ball is hitting paddle height
        if (y >= paddle.ty - r) {
            //Is ball hitting paddle?
            if (x >= paddle.lx && x <= paddle.rx && y < paddle.by) {
                this.xvel = (x - paddle.x) * 9.0 / (paddle.width / 2.0);
                if (this.xvel >= 0.0 && this.xvel <= 0.5) {
                    this.xvel = 0.5;
                }else if(this.xvel <= 0.0 && this.xvel >= -0.51){
                    this.xvel = -0.51;
                }
                this.yvel = -Math.abs(yvel);
                this.y = paddle.ty-r-1;
                return 2;
            } else {
                this.x = (int) Math.round(x + xvel);
                this.y = (int) Math.round(y + yvel);
                return -1;
            }
        }
        if(this.yvel == 0){
            this.x = paddle.x+stickyPaddleOffset;
        }
        //ball is hitting top of screen
        else if (y <= r) {
            this.yvel = Math.abs(yvel);
            this.y = r+1;
        }
        if(!collidedWithBlock) {
            this.x = (int) Math.round(x + xvel);
            this.y = (int) Math.round(y + yvel);
        }else{
            return 1;
        }
        return 0;
    }

    private boolean checkBlockCollisions(ArrayList<Block> blocks){
        int newSideX=0,newSideY=0,newTopX=0,newTopY=0;
        boolean collidedSide = false;
        boolean collidedTop = false;
        for(int i = 0; i < blocks.size(); i++){
            Block block = blocks.get(i);
            boolean collidedWithBlock = false;
            if(!collidedSide) {
                if (xvel > 0) {
                    if (block.lx >= x + r && block.lx <= x + r + xvel) {
                        double perc = (block.lx - (x + r)) / xvel;
                        newSideX = (int)Math.round(block.lx) - r;
                        newSideY = y + (int)Math.round(yvel * perc);
                        if(newSideY <= block.by && newSideY >= block.ty) {
                            collidedSide = true;
                            collidedWithBlock = true;
                        }
                    }
                } else if (xvel < 0) {
                    if (block.rx <= x - r && block.rx >= x - r + xvel) {
                        double perc = ((x - r) - block.rx) / xvel;
                        newSideX = (int)Math.round(block.rx) + r;
                        newSideY = y + (int) Math.round(yvel * perc);
                        if(newSideY <= block.by && newSideY >= block.ty){
                            collidedSide = true;
                            collidedWithBlock = true;
                        }
                    }
                }
            }
            if(!collidedTop) {
                if (yvel > 0) {
                    if (block.ty >= y + r && block.ty <= y + r + yvel) {
                        double perc = (block.ty - (y + r)) / yvel;
                        newTopY = (int)Math.round(block.ty) - r;
                        newTopX = x + (int) Math.round(xvel * perc);
                        if(newTopX >= block.lx && newTopX <= block.rx){
                            collidedTop = true;
                            collidedWithBlock = true;
                        }
                    }

                } else if (yvel < 0) {
                    if (block.by <= y - r && block.by >= y - r + yvel) {
                        double perc = ((y - r) - block.by) / yvel;
                        newTopY = (int)Math.round(block.by) + r;
                        newTopX = x + (int) Math.round(xvel * perc);
                        if(newTopX >= block.lx && newTopX <= block.rx){
                            collidedTop = true;
                            collidedWithBlock = true;
                        }
                    }
                }
            }
            if(collidedWithBlock){
                block.hit();
                if(doubleDamage && !tempBall){
                    block.hit();
                }
                if(block.health <= 0){
                    blocks.remove(i);
                    i--;
                }
                if(tempBall){
                    return true;
                }
            }
        }
        if(collidedSide && collidedTop){
            this.x = newSideX;
            this.y = newTopY;
            this.xvel = -this.xvel;
            this.yvel = -this.yvel;
            return true;
        }else if(collidedSide){
            this.x = newSideX;
            this.y = newSideY;
            this.xvel = -this.xvel;
            return true;
        }else if(collidedTop){
            this.x = newTopX;
            this.y = newTopY;
            this.yvel = -this.yvel;
            return true;
        }
        return false;
    }

    //Draws a filled circle at x,y with radius r
    public void paint(Paint paint, Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        if(doubleDamage){
            paint.setColor(Color.RED);
        }
        canvas.drawCircle(x, y, r, paint);
    }
}
