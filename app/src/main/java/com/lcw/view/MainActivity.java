package com.lcw.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lcw.view.shoppingcart.GoodsView;

public class MainActivity extends AppCompatActivity {

    private Button bt_shoppingcart_goods;
    private Button bt_shoppingcart_cart;
    private ViewGroup mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        initView();
        getData();


    }

    private void getData() {
        bt_shoppingcart_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取商品坐标
                int[] goodsPoint = new int[2];
                bt_shoppingcart_goods.getLocationInWindow(goodsPoint);

                //获取购物车坐标
                int[] shoppingCartPoint = new int[2];
                bt_shoppingcart_cart.getLocationInWindow(shoppingCartPoint);


                //生成商品View
                GoodsView goodsView = new GoodsView(MainActivity.this);
                goodsView.setCircleStartPoint(goodsPoint[0], goodsPoint[1]);
                goodsView.setCircleEndPoint(shoppingCartPoint[0], shoppingCartPoint[1]);
                //添加View并执行动画
                mViewGroup.addView(goodsView);
                goodsView.startAnimation();

            }
        });

    }

    private void initView() {
        bt_shoppingcart_goods = (Button) findViewById(R.id.bt_shoppingcart_goods);
        bt_shoppingcart_cart = (Button) findViewById(R.id.bt_shoppingcart_cart);
        mViewGroup = (ViewGroup) getWindow().getDecorView();

    }


}
