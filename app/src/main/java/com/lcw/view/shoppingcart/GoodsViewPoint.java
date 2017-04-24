package com.lcw.view.shoppingcart;

/**
 * 记录小红点的x,y相关坐标
 * Create by: chenwei.li
 * Date: 2017/4/23
 * Time: 下午10:13
 * Email: lichenwei.me@foxmail.com
 */

public class GoodsViewPoint {

    private int x;
    private int y;

    public GoodsViewPoint() {
    }

    public GoodsViewPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "GoodsViewPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
