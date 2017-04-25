package com.lcw.view;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lcw.view.shoppingcart.GoodsView;
import com.lcw.view.shoppingcart.ShoppingCartActivity;
import com.lcw.view.starview.StarView;
import com.lcw.view.starview.StarViewActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_main_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShoppingCartActivity.class));
            }
        });

        findViewById(R.id.bt_main_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StarViewActivity.class));
            }
        });


    }


}
