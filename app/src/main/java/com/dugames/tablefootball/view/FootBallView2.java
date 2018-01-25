package com.dugames.tablefootball.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.action;
import static android.R.attr.id;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.content.ContentValues.TAG;

/**
 * Created by liruibao on 2017/9/6.
 */

public class FootBallView2 extends View {
    private static final int playerNum = 2; // 球员的数量
    private static final float[] playerRedPosY = {1/8,1/8,2/8,3/8,3/8};
    private static final float[] playerBluePosY = {5/8,5/8,6/8,7/8,7/8};

    private static final float[] playerRedPosX = {1/6,5/6,1/2,1/6,5/6};
    private static final float[] playerBluePosX = {1/6,5/6,1/2,1/6,5/6};
    private static final int padding = 10;

    private List<Point> points = new ArrayList<>(18);
    private int mWidth = 0;
    private int mHeight = 0;
    private Ball mBall;
    private Player[] mPlayerRed = new Player[playerNum];
    private Player[] mPlayerBlue = new Player[playerNum];

    private Goal goalRed = new Goal();
    private Goal goalBlue = new Goal();
    private boolean isFirst = true;
    private boolean isStop = false;

    private Context mContext;

    public FootBallView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("sss", getMeasuredHeight() + "   " + getMeasuredWidth());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();

        initScene(canvas);

        if (isFirst) {

            initPlayer(canvas);
            initBall(canvas);
            isFirst = false;
        } else if (!isStop) {

            collisionDetectingAndChangeSpeed(mBall);
            mBall.collisionPlayerDetect(mPlayerRed, mPlayerBlue);
            mBall.move();
            drawBall(canvas);
            drawPlayer(canvas);
            if (mBall.detect(goalBlue) || mBall.detect(goalRed)) {
                isStop = true;
//                Toast.makeText(mContext, mBall.detect(goalBlue) ? "红队胜利" : "蓝队胜利", Toast.LENGTH_LONG).show();
                shopPoup(mBall.detect(goalBlue) ? "红队胜利" : "蓝队胜利");
            }
        }


        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        // 16毫秒执行一次
        if (!isStop) {

            postInvalidateDelayed(Math.abs(runTime - 20));
        }
        Log.i("sss", getMeasuredHeight() + "   " + getMeasuredWidth());
    }

    public void shopPoup(String msg) {
        AlertDialog dlg = new AlertDialog.Builder(mContext).setTitle("游戏结束")//设置对话框标题

                .setMessage("本次游戏结束,\n" + msg + "，\n是否重新开始新的一局")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        restart();
                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }

                }).show();//在按键响应事件中显示此对话框

    }

    private void initBall(Canvas canvas) {
        mBall = new Ball();
        mBall.cx = (mWidth - 20) / 2;
        mBall.cy = (mHeight - 20) / 2;
        mBall.radius = (mHeight - 20) / 32;
        Random r = new Random();

        mBall.vx = 20;
        if (r.nextInt(2) < 1) {
            mBall.vx = -20;
        }
        mBall.vy = 20;
        if (r.nextInt(2) < 1) {
            mBall.vy = -20;
        }

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        mBall.paint = paint;
        canvas.drawCircle(mBall.cx, mBall.cy, mBall.radius, mBall.paint);

    }

    private void initPlayer(Canvas canvas) {
        int realWidth = mWidth - 2*padding;
        int realHeight = mHeight - 2*padding;


        mPlayerRed[0] = new Player();
        mPlayerRed[0].cx = padding + realWidth / 6;
        mPlayerRed[0].cy = realHeight / 8;
        mPlayerRed[0].widthLimit[0] = padding;
        mPlayerRed[0].widthLimit[1] = padding + realWidth / 3;

//        mPlayerRed[1] = new Player();
//        mPlayerRed[1].cx = padding + realWidth / 6 * 5;
//        mPlayerRed[1].cy = realHeight / 8;
//        mPlayerRed[1].widthLimit[0] = padding + realWidth / 3 * 2;
//        mPlayerRed[1].widthLimit[1] = mWidth - padding;

//        mPlayerRed[2] = new Player();
//        mPlayerRed[2].cx = padding + realWidth / 2;
//        mPlayerRed[2].cy = realHeight / 4;
//        mPlayerRed[2].widthLimit[0] = padding + realWidth / 4;
//        mPlayerRed[2].widthLimit[1] = padding + realWidth / 4 * 3;
//
//
//        mPlayerRed[3] = new Player();
//        mPlayerRed[3].cx = padding + realWidth / 6;
//        mPlayerRed[3].cy = realHeight / 8 * 3;
//        mPlayerRed[3].widthLimit[0] = padding;
//        mPlayerRed[3].widthLimit[1] = padding + realWidth / 3;
//
//        mPlayerRed[4] = new Player();
//        mPlayerRed[4].cx = padding + realWidth / 6 * 5;
//        mPlayerRed[4].cy = realHeight / 8 * 3;
//        mPlayerRed[4].widthLimit[0] = padding + realWidth / 3 * 2;
//        mPlayerRed[4].widthLimit[1] = mWidth - padding;

        mPlayerRed[1] = new Player();
        mPlayerRed[1].cx = padding + realWidth / 6 * 5;
        mPlayerRed[1].cy = realHeight / 8 * 3;
        mPlayerRed[1].widthLimit[0] = padding + realWidth / 3 * 2;
        mPlayerRed[1].widthLimit[1] = mWidth - padding;

        for (int i = 0; i < playerNum; i++) {
            mPlayerRed[i].radius = realHeight / 15;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            mPlayerRed[i].paint = paint;
        }

        for (int i = 0; i < playerNum; i++) {
            Player player = mPlayerRed[i];
            canvas.drawCircle(player.cx, player.cy, player.radius, player.paint);
        }


        mPlayerBlue[0] = new Player();
        mPlayerBlue[0].cx = padding + realWidth / 6;
        mPlayerBlue[0].cy = realHeight / 8 * 7;
        mPlayerBlue[0].widthLimit[0] = padding;
        mPlayerBlue[0].widthLimit[1] = padding + realWidth/ 3;

        mPlayerBlue[1] = new Player();
        mPlayerBlue[1].cx = padding + realWidth / 6 * 5;
        mPlayerBlue[1].cy = realHeight / 8 * 7;
        mPlayerBlue[1].widthLimit[0] = padding + realWidth/ 3 * 2;
        mPlayerBlue[1].widthLimit[1] = mWidth - padding;

//        mPlayerBlue[2] = new Player();
//        mPlayerBlue[2].cx = 10 + realWidth / 2;
//        mPlayerBlue[2].cy = realHeight / 8 * 6;
//        mPlayerBlue[2].widthLimit[0] = padding + realWidth / 4;
//        mPlayerBlue[2].widthLimit[1] = padding + realWidth / 4 * 3;
//
//        mPlayerBlue[3] = new Player();
//        mPlayerBlue[3].cx = padding + realWidth / 6;
//        mPlayerBlue[3].cy = realHeight / 8 * 5;
//        mPlayerBlue[3].widthLimit[0] = padding;
//        mPlayerBlue[3].widthLimit[1] = padding + realWidth / 3;
//
//        mPlayerBlue[4] = new Player();
//        mPlayerBlue[4].cx = padding + realWidth / 6 * 5;
//        mPlayerBlue[4].cy = realHeight / 8 * 5;
//        mPlayerBlue[4].widthLimit[0] = padding + realWidth / 3 * 2;
//        mPlayerBlue[4].widthLimit[1] = mWidth - padding;
        for (int i = 0; i < playerNum; i++) {
            mPlayerBlue[i].radius = realHeight / 15;
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(2);
//            Paint paint=new Paint();  //定义一个Paint
            Shader mShader = new LinearGradient(0,0,40,60,new int[] {Color.RED,Color.GREEN,Color.BLUE},null,Shader.TileMode.REPEAT);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变

            paint.setShader(mShader);
            mPlayerBlue[i].paint = paint;
        }

        for (int i = 0; i < playerNum; i++) {
            Player player = mPlayerBlue[i];
            canvas.drawCircle(player.cx, player.cy, player.radius, player.paint);
        }
    }

    private void initScene(Canvas canvas) {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        int realWidth = mWidth - 2*padding;
        int realHeight = mHeight - 2*padding;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        for (int i = 0; i < 18; i += 2) {
            points.add(new Point(padding, padding + i / 2 * realHeight / 8));
            points.add(new Point(mWidth - padding, padding + i / 2 * realHeight / 8));
            canvas.drawLine(padding, padding + i / 2 * realHeight / 8, mWidth - padding, padding + i / 2 * realHeight / 8, paint);
        }
        canvas.drawLine(padding, padding, padding, mHeight - padding, paint);
        canvas.drawLine(mWidth - padding, padding, mWidth - padding, mHeight - padding, paint);

        goalRed.cx = realWidth / 2;
        goalRed.width = realWidth / 5;
        goalRed.height = realHeight / 25;
        goalRed.cy = padding + goalRed.height / 2;
        Paint paintGoal = new Paint();
        paintGoal.setColor(Color.RED);
        paintGoal.setStrokeWidth(2);
        goalRed.paint = paintGoal;
        canvas.drawRect(new Rect(goalRed.cx - goalRed.width / 2, goalRed.cy - goalRed.height / 2,
                goalRed.cx + goalRed.width / 2, goalRed.cy + goalRed.height / 2), goalRed.paint);

        goalBlue.cx = realWidth / 2;
        goalBlue.width = realWidth / 5;
        goalBlue.height = realHeight / 25;
        goalBlue.cy = (mHeight - padding) - goalBlue.height / 2;

        Paint paintBlue = new Paint();
        paintBlue.setColor(Color.BLUE);
        paintBlue.setStrokeWidth(2);
        goalBlue.paint = paintBlue;
        canvas.drawRect(new Rect(goalBlue.cx - goalBlue.width / 2, goalBlue.cy - goalBlue.height / 2,
                goalBlue.cx + goalBlue.width / 2, goalBlue.cy + goalBlue.height / 2), goalBlue.paint);

    }

    private void drawPlayer(Canvas canvas) {
        Random r = new Random();
        for (int i = 0; i < playerNum; i++) {
            Player player = mPlayerRed[i];

//            player.vx = r.nextInt(100)-50;
//            player.move();
            player.robotDetect(mBall);
            canvas.drawCircle(player.cx, player.cy, player.radius, player.paint);

        }
        for (int i = 0; i < playerNum; i++) {
            Player player = mPlayerBlue[i];
            canvas.drawCircle(player.cx, player.cy, player.radius, player.paint);
        }
    }

    private void drawBall(Canvas canvas) {
        canvas.drawCircle(mBall.cx, mBall.cy, mBall.radius, mBall.paint);

    }

    public class Player {
        private int pointerId = -1;
        private float oldX = 0;
        private float oldY = 0;
        private int cx;
        private int cy;
        private int radius;
        private int[] widthLimit = new int[2];
        float vx = 0; // X轴速度
        float vy = 0; // Y轴速度
        Paint paint;

        void move() {
            cx += vx;
            move(cx,0);
//            if (cx > widthLimit[0] && cx < widthLimit[1]) {
//
//            } else if (cx < widthLimit[0]) {
//                cx = widthLimit[0];
//            } else if (cx > widthLimit[1]) {
//                cx = widthLimit[1];
//            }
        }
        // 移动
        void moveRobot() {
            cx += vx;
            //向角度的方向移动，偏移圆心

        }

        // 移动
        void move(float x, float y) {
            //向角度的方向移动，偏移圆心
//            if (x > widthLimit[0] && x < widthLimit[1]) {
//                cx = (int) x;
//            } else if (x < widthLimit[0]) {
//                cx = widthLimit[0];
//            } else if (x > widthLimit[1]) {
//                cx = widthLimit[1];
//            }
            if (x < padding ){
                cx = padding;
            }else if (x > mWidth - padding){
                cx = mWidth - padding;
            }else {
                cx = (int)x;
            }

            if (y > mHeight -padding){
                cy = mHeight -padding;
            }else if (y-radius < mHeight/2){
                cy = mHeight/2 + radius;
            }else {
                cy = (int)y;
            }

        }

        int left() {
            return (int) (cx - radius);
        }

        int right() {
            return (int) (cx + radius);
        }

        int bottom() {
            return (int) (cy + radius);
        }

        int top() {
            return (int) (cy - radius);
        }

        private boolean touchDetect(float x, float y) {
            if (oldX == 0 && oldY == 0) {
                if ((x - cx) * (x - cx) + (y - cy) * (y - cy) < radius * radius) {
                    return true;
                }
            } else {
                if (x - oldY > 0) {
                    vx = 10;
                } else {
                    vx = -10;
                }
                oldY = x;
                if ((x - cx) * (x - cx) + (y - cy) * (y - cy) < radius * radius) {
                    return true;
                }
            }

            return false;
        }

        private void robotDetect(Ball ball) {
            if (ball.vy < 0 && cy < ball.cy) {
                if (cx < ball.cx) {

                    vx = cx + radius - ball.cx + ball.radius < 0 ? 10 : 0;
                }else{
                    vx = cx - radius - ball.cx - ball.radius > 0 ? -10 : 0;
                }
            } else if (ball.vy > 0 && cy > ball.cy) {
                if (cx < ball.cx){
                    vx = ball.cx - ball.radius -cx - radius < 0 ?  -10 : 0;
                }else{
                    vx =cx-radius-ball.cx-ball.radius < 0?  10 : 0;
                }

            } else {
                vx = 0;
            }
            moveRobot();
        }


    }

    public class Ball {
        private int cx;
        private int cy;
        private int radius;
        float vx; // X轴速度
        float vy; // Y轴速度
        Paint paint;

        // 移动
        void move() {
            //向角度的方向移动，偏移圆心
            if(vx > 20){
                vx -=1;
            }
            if (vx > 30){
                vx = 20;
            }
            if(vy > 20){
                vy -=1;
            }
            if (vy > 30){
                vy = 20;
            }
            cx += vx;
            cy += vy;
        }


        int left() {
            return (int) (cx - radius);
        }

        int right() {
            return (int) (cx + radius);
        }

        int bottom() {
            return (int) (cy + radius);
        }

        int top() {
            return (int) (cy - radius);
        }

        private void collisionPlayerDetect(Player[] red, Player[] blue) {
            for (Player p : red) {
                collision(p);
            }
            for (Player p : blue) {
                collision(p);
            }

        }

        public void collision(Player p) {
            double at = Math.atan2(-(cy - p.cy), cx - p.cx);
            double v = Math.sqrt(vx * vx + vy * vy);
            if (istouch(this, p)) {
                vy = -(float) (v * Math.sin(at)) ;
                vx = (float) (v * Math.cos(at));
                vx = vx > 0?vx+10:vx-10;
                vy = vy > 0?vy+10:vy-10;
                if (vy < 1 && vy > -1) {
                    vy = 5;
                }
                if (vx < 1 && vx > -1) {
                    vx = 5;
                }
                while (istouch(this, p)) {
                    move();
                }
                return;
            }
        }

        public boolean istouch(Ball b1, Player p) {
            return (b1.cx - p.cx) * (b1.cx - p.cx) + (b1.cy - p.cy) * (b1.cy - p.cy) < (b1.radius + p.radius) * (b1.radius + p.radius);
        }

        public boolean detect(Goal goal) {
            if (cx > goal.cx - goal.width / 2 &&
                    cx < goal.cx + goal.width / 2 &&
                    cy > goal.cy - goal.height / 2 &&
                    cy < goal.cy + goal.height / 2
                    ) {
                return true;
            }
            return false;

        }


    }

    public class Goal {
        private int cx;
        private int cy;
        private int width;
        private int height;
        Paint paint;

    }

    // 判断球是否碰撞碰撞边界
    public void collisionDetectingAndChangeSpeed(Ball ball) {
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();

        float speedX = ball.vx;
        float speedY = ball.vy;

        // 碰撞左右，X的速度取反。 speed的判断是防止重复检测碰撞，然后黏在墙上了=。=
        if (ball.left() <= left && speedX < 0) {
            ball.vx = -ball.vx;
        } else if (ball.top() <= top && speedY < 0) {
            ball.vy = -ball.vy;
        } else if (ball.right() >= right && speedX > 0) {
            ball.vx = -ball.vx;
        } else if (ball.bottom() >= bottom && speedY > 0) {
            ball.vy = -ball.vy;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointId = event.getPointerId(event.getActionIndex());
        int count = event.getPointerCount();
//
//
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                // 判断是否是第2个手指按下
////                for (Player p : mPlayerBlue) {
////                    if (p.touchDetect(x, y)){
////                        p.pointerId = pointId;
////                    }
////                }
//                break;
//            case MotionEvent.ACTION_UP:
//
////                for (Player p : mPlayerBlue) {
////                    if (p.pointerId == pointId){
////                        p.pointerId = -1;
////                    }
////                }
//                // 判断抬起的手指是否是第2个
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                // 判断是否是第2个手指按下
////                for (Player p : mPlayerBlue) {
////                    if (p.touchDetect(x, y)){
////                        p.pointerId = pointId;
////                    }
////                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//
////                for (Player p : mPlayerBlue) {
////                    if (p.pointerId == pointId){
////                        p.pointerId = -1;
////                    }
////                }
//                // 判断抬起的手指是否是第2个
//                break;
//            case MotionEvent.ACTION_MOVE:
//                for (int i = 0;i<count;i++){
//                    float x = event.getX(i);
//                    float y = event.getY(i);
//                    for (Player p : mPlayerBlue) {
//                        if (p.touchDetect(x, y)){
//                            p.move(x,y);
//                        }
//                    }
//                }
//
////                int pointId = event.getPointerId(event.getActionIndex());
////                touchDetect(x, y,pointId);
//                break;
//        }
        for (int i = 0;i<count;i++){
            float x = event.getX(i);
            float y = event.getY(i);
            for (Player p : mPlayerBlue) {
                if (p.touchDetect(x, y)){
                    p.move(x,y);
                    break;
                }
            }
        }

        return true;
    }

    private void touchDetect(float x, float y , int id) {

        for (Player p : mPlayerRed) {
            if (p.touchDetect(x, y)) {
                p.move(x, y);
                p.pointerId = id;
            }
        }
        for (Player p : mPlayerBlue) {
           if (p.pointerId == id && p.touchDetect(x, y)){
               p.move(x, y);
           }
        }
        for (Player p : mPlayerBlue) {
            if (p.touchDetect(x, y)) {
                p.move(x, y);
                p.pointerId = id;
            }
        }

    }

    public void restart() {
        isFirst = true;
        isStop = false;
        postInvalidateDelayed(0);
    }

}
