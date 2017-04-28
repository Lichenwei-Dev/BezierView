package com.lcw.view.starview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lcw.view.R;

import java.util.Random;

/**
 * 自定义ViewGroup（内含自定义View-星星）
 * Create by: chenwei.li
 * Date: 2017/4/25
 * time: 15:41
 * Email: lichenwei.me@foxmail.com
 */
public class StarViewGroup extends RelativeLayout implements View.OnClickListener {

    private Bitmap mBitmap;

    //画笔，路径
    private Paint mPaint;
    private Path mPath;

    //记录屏幕的宽，高
    private int mScreenWidth;
    private int mScreenHeight;

    //记录数据点，控制点(由于是三阶贝塞尔曲线，所以有2个控制点)
    protected Point mStartPoint;
    protected Point mEndPoint;
    protected Point mConOnePoint;
    protected Point mConTwoPoint;

    protected Random mRandom;
    protected int[] mColors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA, Color.YELLOW};


    public StarViewGroup(Context context) {
        super(context);
        initView();
    }

    public StarViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 进行一些初始化的操作
     */
    private void initView() {
        //初始化画笔，路径
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPath = new Path();
        mRandom = new Random();
        //获取资源图片转化Bitmap（不可修改）
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_star);

        setOnClickListener(this);
    }

    /**
     * 画星星并随机赋予不同的颜色
     *
     * @param color
     * @return
     */
    private Bitmap drawStar(int color) {
        //创建和资源文件Bitmap相同尺寸的Bitmap填充Canvas
        Bitmap outBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        //利用Graphics中的XferModes对Canvas进行着色
        canvas.drawColor(color, PorterDuff.Mode.SRC_IN);
        canvas.setBitmap(null);
        return outBitmap;
    }

    protected void addStar() {
        Bitmap starBitmap = drawStar(mColors[mRandom.nextInt(mColors.length)]);
        final ImageView imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(120, 100);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        imageView.setImageBitmap(starBitmap);
        addView(imageView, layoutParams);


        Point conOnePoint = this.mConOnePoint;
        Point conTwoPoint = this.mConTwoPoint;
        Point startPoint = this.mStartPoint;
        Point endPoint = this.mEndPoint;


        //设置属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new StarTypeEvaluator(conOnePoint, conTwoPoint), startPoint,
                endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                imageView.setX(point.x);
                imageView.setY(point.y);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                StarViewGroup.this.removeView(imageView);
            }
        });


        //透明度动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0f);

        //组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(4000);
        animatorSet.play(valueAnimator).with(objectAnimator);
        animatorSet.start();


        valueAnimator.start();
    }

    /**
     * 获取屏幕的宽高并设置对应的数据点和控制点
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mScreenWidth = w;
        this.mScreenHeight = h;
        mStartPoint = new Point(mScreenWidth / 2, mScreenHeight);
        mEndPoint = new Point(mScreenWidth / 2, 0);
        mConOnePoint = new Point(mScreenWidth, mScreenHeight * 3 / 4);
        mConTwoPoint = new Point(0, mScreenHeight / 4);
        //为了调用onDraw方法
        setBackgroundColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPath.moveTo(mStartPoint.x, mStartPoint.y);
//        mPath.cubicTo(mConOnePoint.x, mConOnePoint.y, mConTwoPoint.x, mConTwoPoint.y, mEndPoint.x, mEndPoint.y);
//        canvas.drawPath(mPath, mPaint);
    }


    class StarTypeEvaluator implements TypeEvaluator<Point> {

        //记录控制点
        private Point conOnePoint, conSecondPoint;

        public StarTypeEvaluator(Point conOnePoint, Point conSecondPoint) {
            this.conOnePoint = conOnePoint;
            this.conSecondPoint = conSecondPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {

            //利用三阶贝塞尔曲线公式算出中间点坐标
            int x = (int) (startValue.x * Math.pow((1 - t), 3) + 3 * conOnePoint.x * t * Math.pow((1 - t), 2) + 3 *
                    conSecondPoint.x * Math.pow(t, 2) * (1 - t) + endValue.x * Math.pow(t, 3));
            int y = (int) (startValue.y * Math.pow((1 - t), 3) + 3 * conOnePoint.y * t * Math.pow((1 - t), 2) + 3 *
                    conSecondPoint.y * Math.pow(t, 2) * (1 - t) + endValue.y * Math.pow(t, 3));
            return new Point(x, y);
        }
    }

    @Override
    public void onClick(View v) {

//        mStartPoint = new Point(mScreenWidth / 2, mScreenHeight);
//        mEndPoint = new Point((int) (mScreenWidth / 2 + 150 * mRandom.nextFloat()), 0);
//        mConOnePoint = new Point((int) (mScreenWidth * mRandom.nextFloat()), (int) (mScreenHeight * 3 * mRandom.nextFloat() / 4));
//        mConTwoPoint = new Point(0, (int) (mScreenHeight * mRandom.nextFloat() / 4));
//
//        addStar();
    }

    /**
     * 监听onTouch事件，动态生成对应坐标
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mStartPoint = new Point(mScreenWidth / 2, mScreenHeight);
        mEndPoint = new Point((int) (mScreenWidth / 2 + 150 * mRandom.nextFloat()), 0);
        mConOnePoint = new Point((int) (mScreenWidth * mRandom.nextFloat()), (int) (mScreenHeight * 3 * mRandom.nextFloat() / 4));
        mConTwoPoint = new Point(0, (int) (mScreenHeight * mRandom.nextFloat() / 4));

        addStar();
        return true;
    }
}
