package com.lcw.view.shoppingcart;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 商品动画自定义View
 * Create by: chenwei.li
 * Date: 2017/4/23
 * Time: 下午9:21
 * Email: lichenwei.me@foxmail.com
 */

public class GoodsView extends View {

    //小红点开始坐标
    GoodsViewPoint mCircleStartPoint = new GoodsViewPoint();
    //小红点结束坐标
    GoodsViewPoint mCircleEndPoint = new GoodsViewPoint();
    //小红点控制点坐标
    GoodsViewPoint mCircleConPoint = new GoodsViewPoint();
    Path path = new Path();
    //小红点半径
    private int mRadius = 30;
    //小红点画笔
    private Paint mCirclePaint;

    private Paint mLinePaint;


    public GoodsView(Context context) {
        super(context);
        init();
    }


    public GoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);

    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.RED);


        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setColor(Color.RED);
    }

    /**
     * 商品飞入购物车的小红点
     */
    private void drawCircle(Canvas canvas) {
//        path.moveTo(mCircleStartPoint.getX(), mCircleStartPoint.getY());
//        path.quadTo(mCircleConPoint.getX(), mCircleConPoint.getY(), mCircleEndPoint.getX(), mCircleEndPoint.getY());
//        canvas.drawPath(path, mLinePaint);
        canvas.drawCircle(mCircleStartPoint.getX(), mCircleStartPoint.getY(), mRadius, mCirclePaint);

    }


    public void setCircleStartPoint(int x, int y) {
        Log.i("Rabbit", x + "-" + y);
        this.mCircleStartPoint.setX(x);
        this.mCircleStartPoint.setY(y);
    }

    public void setCircleEndPoint(int x, int y) {
        Log.i("Rabbit", x + "--" + y);
        this.mCircleEndPoint.setX(x);
        this.mCircleEndPoint.setY(y);
    }


    public void startAnimation() {
        if (mCircleStartPoint == null || mCircleEndPoint == null) {
            return;
        }
        mCircleConPoint.setX((mCircleStartPoint.getX() + mCircleEndPoint.getX()) / 2);
        mCircleConPoint.setY(mCircleStartPoint.getY() - 50);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new CirclePointEvaluator(), mCircleStartPoint, mCircleEndPoint);
        valueAnimator.setDuration(1200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                GoodsViewPoint goodsViewPoint = (GoodsViewPoint) animation.getAnimatedValue();
                setX(goodsViewPoint.getX());
                setY(goodsViewPoint.getY());
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
         * @param fraction   当前动画进度
         * @param startValue 开始值
         * @param endValue   结束值
         * @return
         */
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {

            GoodsViewPoint startPoint = (GoodsViewPoint) startValue;
            GoodsViewPoint endPoint = (GoodsViewPoint) endValue;

            int x = (int) (((1 - fraction) * (1 - fraction)) * startPoint.getX() + 2 * fraction * (1 - fraction) * mCircleConPoint.getX() + fraction * fraction * endPoint.getX());
            int y = (int) (((1 - fraction) * (1 - fraction)) * startPoint.getY() + 2 * fraction * (1 - fraction) * mCircleConPoint.getY() + fraction * fraction * endPoint.getY());

            Log.i("Rabbit", x + "--" + y);

            GoodsViewPoint goodsViewPoint = new GoodsViewPoint();
            goodsViewPoint.setX(x);
            goodsViewPoint.setY(y);
            return goodsViewPoint;
        }
    }
}
