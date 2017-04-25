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
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lcw.view.R;

/**
 * 自定义ViewGroup（内含自定义View-星星）
 * Create by: chenwei.li
 * Date: 2017/4/25
 * time: 15:41
 * Email: lichenwei.me@foxmail.com
 */
public class StarView extends RelativeLayout implements View.OnClickListener{

    private Bitmap mBitmap;

    //画笔，路径
    private Paint mPaint;
    private Path mPath;

    //记录屏幕的宽，高
    private int mScreenWidth;
    private int mScreenHeight;

    //记录数据点，控制点(由于是三阶贝塞尔曲线，所以有2个控制点)
    private Point mStartPoint;
    private Point mEndPoint;
    private Point mConOnePoint;
    private Point mConTwoPoint;


    public StarView(Context context) {
        super(context);
        initView();
    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Bitmap starBitmap = drawStar(Color.BLUE);
        final ImageView imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams=new LayoutParams(80, 80);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        imageView.setImageBitmap(starBitmap);
        addView(imageView,layoutParams);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new StarTypeEvaluator(), mStartPoint, mEndPoint);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
                StarView.this.removeView(imageView);
            }
        });


        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(imageView,"alpha",1.0f,0.2f);
        objectAnimator.setDuration(800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(1300);
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
        mPath.moveTo(mStartPoint.x,mStartPoint.y);
        mPath.cubicTo(mConOnePoint.x,mConOnePoint.y,mConTwoPoint.x,mConTwoPoint.y,mEndPoint.x,mEndPoint.y);
        canvas.drawPath(mPath,mPaint);
    }


    class StarTypeEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float temp = 1 - fraction;
            int x = (int) (temp * temp * temp * startValue.x + 3 * temp * temp * fraction * mConOnePoint.x + 3 * temp*
                    fraction * fraction * mConTwoPoint.x + fraction * fraction * fraction * mEndPoint.x);
            int y = (int) (temp * temp * temp * startValue.y + 3 * temp * temp * fraction * mConOnePoint.y + 3 * temp *
                    fraction * fraction * mConTwoPoint.y + fraction * fraction * fraction * mEndPoint.y);
            return new Point(x, y);
        }
    }

    @Override
    public void onClick(View v) {
        addStar();
    }


}
