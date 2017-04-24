package com.lcw.view.shoppingcart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    //小红点的移动坐标
    GoodsViewPoint mCircleMovePoint = new GoodsViewPoint();
    //小红点半径
    private int mRadius=20;
    //小红点画笔
    private Paint mCirclePaint;

    public GoodsView(Context context) {
        super(context);
        init(context);
    }


    public GoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    /**
     * 进行一些初始化操作
     */
    private void init(Context context) {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.RED);

    }

    /**
     * 商品加入购物车的小红点
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mCircleMovePoint.getX(), mCircleMovePoint.getY(), mRadius, mCirclePaint);
    }

    /**
     * 设置开始点和开始移动点
     * @param x
     * @param y
     */
    public void setCircleStartPoint(int x, int y) {
        this.mCircleStartPoint.setX(x);
        this.mCircleStartPoint.setY(y);
        this.mCircleMovePoint.setX(x);
        this.mCircleMovePoint.setY(y);
    }

    /**
     * 设置结束点
     * @param x
     * @param y
     */
    public void setCircleEndPoint(int x, int y) {
        this.mCircleEndPoint.setX(x);
        this.mCircleEndPoint.setY(y);
    }


    /**
     * 开始动画
     */
    public void startAnimation() {
        if (mCircleStartPoint == null || mCircleEndPoint == null) {
            return;
        }

        //设置控制点
        mCircleConPoint.setX((mCircleStartPoint.getX() + mCircleEndPoint.getX()) / 2);
        mCircleConPoint.setY(mCircleConPoint.getY() + 20);

        //设置值动画
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new CirclePointEvaluator(), mCircleStartPoint, mCircleEndPoint);
        valueAnimator.setDuration(600);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                GoodsViewPoint goodsViewPoint = (GoodsViewPoint) animation.getAnimatedValue();
                mCircleMovePoint.setX(goodsViewPoint.getX());
                mCircleMovePoint.setY(goodsViewPoint.getY());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewGroup viewGroup= (ViewGroup) getParent();
                viewGroup.removeView(GoodsView.this);
                super.onAnimationEnd(animation);
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

            float temp = 1 - fraction;

            int x = (int) (temp * temp * startPoint.getX() + 2 * fraction * temp * mCircleConPoint.getX() + fraction * fraction
                    * endPoint.getX());
            int y = (int) (temp * temp * startPoint.getY() + 2 * fraction * temp * mCircleConPoint.getY() + fraction * fraction
                    * endPoint.getY());


            GoodsViewPoint goodsViewPoint = new GoodsViewPoint();
            goodsViewPoint.setX(x);
            goodsViewPoint.setY(y);
            return goodsViewPoint;
        }

    }

}
