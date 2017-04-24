package com.lcw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 自定义View(三阶贝塞尔曲线)
 * Create by: chenwei.li
 * Date: 2017/4/21
 * Time: 下午11:47
 * Email: lichenwei.me@foxmail.com
 */

public class BezierCubicView extends View {

    //开始点和结束点
    private int mStartXPoint;
    private int mStartYPoint;
    private int mEndXPoint;
    private int mEndYPoint;
    //控制点
    private int mConOneXPoint;
    private int mConOneYPoint;
    private int mConTwoXPoint;
    private int mConTwoYPoint;

    //路径和画笔
    private Path mPath;
    private Paint mPaint;


    //辅助线画笔,写字画笔
    private Paint mLinePaint;
    private Paint mTextPaint;

    //标志是否按下
    private boolean mFlag;


    public BezierCubicView(Context context) {
        super(context);
        init(context);
    }


    public BezierCubicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BezierCubicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 进行初始化的一些操作
     */
    private void init(Context context) {
        //获取屏幕的宽高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        //设置各点的位置
        mStartXPoint = screenWidth / 4;
        mStartYPoint = screenHeight / 2;
        mEndXPoint = screenWidth * 3 / 4;
        mEndYPoint = screenHeight / 2;
        mConOneXPoint = screenWidth / 2 - 300;
        mConOneYPoint = screenHeight / 2 - 400;
        mConTwoXPoint = screenWidth / 2 + 100;
        mConTwoYPoint = screenHeight / 2 - 400;
        //路径,画笔设置
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        //辅助线画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);

        //写字画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(20);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //贝塞尔曲线
        mPath.moveTo(mStartXPoint, mStartYPoint);
        mPath.cubicTo(mConOneXPoint, mConOneYPoint, mConTwoXPoint, mConTwoYPoint, mEndXPoint, mEndYPoint);
        canvas.drawPath(mPath, mPaint);

        //辅助线
        canvas.drawLine(mStartXPoint, mStartYPoint, mConOneXPoint, mConOneYPoint, mLinePaint);
        canvas.drawLine(mConOneXPoint, mConOneYPoint, mConTwoXPoint, mConTwoYPoint, mLinePaint);
        canvas.drawLine(mConTwoXPoint, mConTwoYPoint, mEndXPoint, mEndYPoint, mLinePaint);

        //文字
        canvas.drawPoint(mStartXPoint, mStartYPoint, mPaint);
        canvas.drawText("起始点", mStartXPoint, mStartYPoint + 30, mTextPaint);
        canvas.drawPoint(mEndXPoint, mEndYPoint, mPaint);
        canvas.drawText("结束点", mEndXPoint, mEndYPoint + 30, mTextPaint);
        canvas.drawPoint(mConOneXPoint, mConOneYPoint, mPaint);
        canvas.drawText("控制点1", mConOneXPoint, mConOneYPoint - 30, mTextPaint);
        canvas.drawPoint(mConTwoXPoint, mConTwoYPoint, mPaint);
        canvas.drawText("控制点2", mConTwoXPoint, mConTwoYPoint - 30, mTextPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mFlag = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mFlag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mConOneXPoint = (int) event.getX(0);
                mConOneYPoint = (int) event.getY(0);
                if (mFlag) {
                    mConTwoXPoint = (int) event.getY(1);
                    mConTwoYPoint = (int) event.getY(1);
                }
                invalidate();
                break;
        }
        return true;
    }
}
