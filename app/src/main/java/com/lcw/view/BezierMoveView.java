package com.lcw.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 自定义View(二阶贝塞尔曲线滑动动画)
 * Create by: chenwei.li
 * Date: 2017/4/21
 * Time: 下午11:47
 * Email: lichenwei.me@foxmail.com
 */

public class BezierMoveView extends View implements View.OnClickListener {

    //开始点和结束点
    private int mStartXPoint;
    private int mStartYPoint;
    private int mEndXPoint;
    private int mEndYPoint;
    //控制点
    private int mConXPoint;
    private int mConYPoint;
    //移动点
    private int mMoveXPoint;
    private int mMoveYPoint;

    //路径和画笔
    private Path mPath;
    private Paint mPaint;

    //圆形半径，画笔
    private int mCircleRadius;
    private Paint mCirlcePaint;


    public BezierMoveView(Context context) {
        super(context);
        init(context);
    }


    public BezierMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BezierMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 进行初始化的一些操作
     */
    private void init(Context context) {

        //设置各点的位置
        mStartXPoint = 100;
        mStartYPoint = 100;
        mEndXPoint = 600;
        mEndYPoint = 600;
        mConXPoint = 400;
        mConYPoint = 0;
        mMoveXPoint = 100;
        mMoveYPoint = 100;

        //路径,画笔设置
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        mCircleRadius = 20;
        mCirlcePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirlcePaint.setColor(Color.BLUE);
        mCirlcePaint.setStyle(Paint.Style.FILL);

        setOnClickListener(this);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //贝塞尔曲线
        mPath.moveTo(mStartXPoint, mStartYPoint);
        mPath.quadTo(mConXPoint, mConYPoint, mEndXPoint, mEndYPoint);
        canvas.drawPath(mPath, mPaint);

        //画圆
        canvas.drawCircle(mStartXPoint, mStartYPoint, mCircleRadius, mCirlcePaint);
        canvas.drawCircle(mEndXPoint, mEndYPoint, mCircleRadius, mCirlcePaint);
        canvas.drawCircle(mEndXPoint, mEndYPoint, mCircleRadius, mCirlcePaint);

    }

    @Override
    public void onClick(View v) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new CirclePointEvaluator(), new Point(mStartXPoint, mStartYPoint),
                new Point(mEndXPoint, mEndYPoint));
        valueAnimator.setDuration(600);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                mMoveXPoint = point.x;
                mMoveYPoint = point.y;
                invalidate();
            }
        });
        valueAnimator.start();

    }

    /**
     * 自定义Evaluator
     */
    public class CirclePointEvaluator implements TypeEvaluator {

        /**
         * @param t          当前动画进度
         * @param startValue 开始值
         * @param endValue   结束值
         * @return
         */
        @Override
        public Object evaluate(float t, Object startValue, Object endValue) {

            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;

            float temp = 1 - t;

            int x = (int) (temp * temp * startPoint.x + 2 * t * temp * mConXPoint + t * t * endPoint.x);
            int y = (int) (temp * temp * startPoint.y + 2 * t * temp * mConYPoint + t * t * endPoint.y);

            return new Point(x,y);
        }

    }
}
