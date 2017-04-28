package com.lcw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 自定义ViewGroup（添加爱心）
 * Create by: chenwei.li
 * Date: 2017/4/25
 * time: 11:10
 * Email: lichenwei.me@foxmail.com
 */
public class HeartView extends LinearLayout {

    private Bitmap mBitmap;
    private Bitmap mOutPutBitmap;
    private Paint mPaint;

    public HeartView(Context context) {
        super(context);
        initView();
    }


    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //源图像
        mBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.heart);
        //创建一个目标图像，由于不能对getResources方法提取到的Bitmap对象进行修改操作，必须复制出一个
        mOutPutBitmap=Bitmap.createBitmap(mBitmap.getWidth(),mBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        //Canvas基于目标图像进行操作
        Canvas canvas=new Canvas(mOutPutBitmap);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        //在canvas上的mOutPutBitmap绘制
        canvas.drawBitmap(mBitmap,0,0,mPaint);
        canvas.drawColor(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        ImageView imageView1=new ImageView(getContext());
        imageView1.setImageBitmap(mOutPutBitmap);


        addView(imageView1);


    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}
