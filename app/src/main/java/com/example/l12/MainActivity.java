package com.example.l12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
//SurfaceView 本身是一个View, 通过canvas画布绘制：
//1.surfaceview是用Zorder排序的，它默认在宿主window后面，surfaceView通过在Window上面"挖洞"（
//设置透明区域）进行显示
//2.surfaceView 拥有独立的surface的（绘图表面）

//  SurfaceView与View的区别
//1.View绘图效率低，用于动画变化较少的程序，而 surfaceview与之相反，用于界面更新频繁的程序
//2.Surface拥有独立的surface，不与其宿主共享同一个surface。surface的UI可以在一个独立的线程
//中单独描述它的绘图表面，以区别它的宿主窗口的绘图表面
//3.surface使用双缓冲机制，播放视频时画面更流畅

//双缓冲机制：
//surfaceView在更新视图时用到了两张Canvas,一张frontCanvas 和一张backCanvas,每次实际显示
//frontCanvas,backCanvas储存上一次更改前的视图。当你在播放这一帧的时候，它已经提前帮你加载好后面一帧了，所以播放起视频很流畅。
//当使用lockCanvas（）获取画布时，得到的实际上是backCanvas 而不是正在显示的 frontCanvas ，
//之后你在获取到的 backCanvas 上绘制新视图，再 unlockCanvasAndPost（canvas）此视图，那么
//上传的这张 canvas 将替换原来的 frontCanvas 作为新的frontCanvas ，原来的 frontCanvas 将切换到后台作为 backCanvas 。
//例如，如果你已经先后两次绘制了视图A和B，那么你再调用 lockCanvas（）获取视图，获得的将是A而不是正在显示的B，
//之后你将重绘的 A 视图上传，那么 A 将取代 B 作为新的 frontCanvas 显示在SurfaceView 上，原来的B则转换为backCanvas。
//相当与多个线程，交替解析和渲染每一帧视频数据。

//获取surfaceholder对象（surfaceview内部类），添加回调监听surface生命周期
//mSurfaceHolder = getHolder();
//mSurfaceHolder.addCallback(this);

//surfaceCreated 回调后启动绘制线程
//只有当native层的Surface创建完毕之后，才可以调用lockCanvas()，否则失败。
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        mDrawThread = new DrawThread();
//        mDrawThread.start();
//    }


//绘制
//      Canvas canvas = mSurfaceHolder.lockCanvas();
//      // 使用canvas绘制内容
//      ...
//      mSurfaceHolder.unlockCanvasAndPost(canvas);

public class MainActivity extends AppCompatActivity {
    private SurfaceView mySurfaceView;
    private SurfaceHolder surfaceHolder;
//    private DrawingThread t = new DrawingThread();
//    private DrawingThread2 t2 = new DrawingThread2();
private DrawCircleThread t = new DrawCircleThread();
//private MoveCircleThread t2 = new MoveCircleThread();
    Canvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySurfaceView = findViewById(R.id.surfaceview);
        surfaceHolder = mySurfaceView.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                   t.start();
                 //  t2.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                t.isDrawing = false;
            }
        });
    }

    private class DrawCircleThread extends Thread {
        public boolean isDrawing = true;



        @Override
        public void run() {

            if ((canvas = surfaceHolder.lockCanvas()) == null) {
                    // can happen when the app is paused.

                }
            canvas.drawColor(Color.WHITE);
            final int X = canvas.getWidth()/2;
            final int Y = canvas.getHeight()/2;
            final int R = X/4;
           
            Paint p = new Paint();
            p.setStrokeWidth(5);
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLUE);
            canvas.drawCircle(X,Y,R,p);
                            try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

//    private class MoveCircleThread extends Thread {
//        @Override
//        public void run() {
//
//            }
//
//
//
//
//    }
//    class DrawingThread extends Thread {
//        public boolean doDrawing = true;
//        final int CIRCLE_TICK_REVERT = 15;
//        @Override
//        public void run() {
//            for(int tick = 0; doDrawing; tick = (tick + 1) % CIRCLE_TICK_REVERT) {
//                Canvas canvas;
//                if ((canvas = surfaceHolder.lockCanvas()) == null) {
//                    // can happen when the app is paused.
//                    continue;
//                }
//                canvas.drawColor(Color.WHITE);
//                final int midX = canvas.getWidth()/2;
//                final int midY = canvas.getHeight()/2;
//                final int RADIUS = midX/5;
//
//                Paint p = new Paint();
//                p.setStrokeWidth(0);
//                p.setStyle(Paint.Style.STROKE);
//                p.setColor(Color.RED);
//                canvas.drawRect(new Rect(midX - RADIUS, midY - RADIUS, midX + RADIUS, midY + RADIUS), p);
//                p.setColor(Color.BLUE);
//                int circleR = RADIUS * (10 + CIRCLE_TICK_REVERT - tick) / 15;
//                canvas.drawCircle(midX, midY, circleR, p);
//
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//        }
//    }
//    // This is added to test the effect of surface locking
//    // After calling surfaceHolder.lockCanvas()
//    class DrawingThread2 extends Thread {
//        public boolean doDrawing = true;
//        final int CIRCLE_TICK_REVERT = 15;
//        @Override
//        public void run() {
//            for(int tick = 0; doDrawing; tick = (tick + 1) % CIRCLE_TICK_REVERT) {
//                Canvas canvas;
//                if ((canvas = surfaceHolder.lockCanvas()) == null) {
//                    // can happen when the app is paused.
//                    System.out.println("oh no");
//                    continue;
//                }
//                canvas.drawColor(Color.BLACK);
//
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//        }
//    }
}
