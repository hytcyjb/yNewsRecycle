package yjbo.yy.ynewsrecycle.mainutil;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * 设置分割线前得提前说明你有没有头部和尾部
 * 条件：1.子item不能有背景图片，有的话就会不显示条目
 * <p>
 * //设置分割线
 * locationList.addItemDecoration((new DividerItemDecoration(this,
 * DividerItemDecoration.VERTICAL_LIST)));
 */
public class DividerGridItemDecorationCopy extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int headCount = 0;//头部布局
    private int footCount = 0;//尾部布局

    public DividerGridItemDecorationCopy(Context context, int mheadCount, int mfootCount) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        this.headCount = mheadCount;
        this.footCount = mfootCount;
        LogUtils.d("=-初始化==" + headCount + "=-==" + footCount);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {

        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    /**
     * 顶部的和尾部的得去除（setAdapter时得传过来），其余的就自己处理得注意第一行将显示顶部的最上一横线显示出来，或者显示最下一行横线
     * 二者的区别:http://www.2cto.com/kf/201109/105626.html
     * 当listView 中 item 比较少，不需要滚动就可以现实全部 二者是等价的。
     * 当item个数多 要滚动时 getChildCount()是当前可见的item个数; getCount()是全部。
     *
     * @author yjbo  @time 2017/3/30 17:28
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        int childCount2 = parent.getAdapter().getItemCount();
        LogUtils.d("00-childCount-h=" + childCount + "==spanCount=" + spanCount + "==childCount2==" + childCount2
                + "=-==" + headCount + "=-==" + footCount);
        for (int i = 0; i < childCount; i++) {
//            if (i == childCount ) continue;
            final View child = parent.getChildAt(i);
            LogUtils.d("==child == null=00=" + i);
            if (child == null) {
                LogUtils.d("==child == null==" + i);
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            if (headCount != 0 && i == 0) {//绘制最顶部的一行，只执行一次，
//                int headHight = 0;
//                for (int j=0;j<headCount;j++){//这是在头部高度相等时可以（*headCount），否则则for循环相加
//                    headHight += parent.getChildAt(j).getBottom();
//                }
//                final int left0 = parent.getChildAt(0).getLeft() - params.leftMargin - mDivider.getIntrinsicWidth();
//                final int right0 = parent.getChildAt(0).getRight() + params.rightMargin
//                        ;
//                final int top0 =  0 + headHight;//child.getBottom() + params.bottomMargin
//                final int bottom0 = top0 + mDivider.getIntrinsicHeight();
//                mDivider.setBounds(left0, top0, right0, bottom0);
//                LogUtils.d("==顶部==" + params.bottomMargin+"===="+child.getBottom()+"----"+mDivider.getIntrinsicHeight()+"=="+left0
//                        +"=="+top0+"=="+right0+"=="+bottom0+"==headCount=="+headCount);
//                mDivider.draw(c);
            }
            if (headCount > i) {//以及每行的第一行
                //以及每行的第一行
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin
                        + mDivider.getIntrinsicWidth();
                final int top = child.getBottom() + params.bottomMargin - mDivider.getIntrinsicHeight();
                final int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                LogUtils.d("==上下左右的边距=111=" + i + "===" + left + "----" + top + "----" + right + "----" + bottom);
                mDivider.draw(c);
            } else {
                if (footCount != 0 && i >= childCount - footCount) {//此时不绘制尾部
                } else {
                    final int left = child.getLeft() - params.leftMargin;
                    final int right = child.getRight() + params.rightMargin;
                    final int top = child.getBottom() + params.bottomMargin - mDivider.getIntrinsicHeight();
                    final int bottom = top + mDivider.getIntrinsicHeight();

                    mDivider.setBounds(left, top, right, bottom);
                    LogUtils.d("==上下左右的边距==" + i + "===" + left + "----" + top + "----" + right + "----" + bottom);
                    mDivider.draw(c);
                }

            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        int childCount2 = parent.getAdapter().getItemCount();
        //11-childCount=11==spanCount=3==childCount2==18 说明childCount2是最完整的，而childCount是不完整的；
        LogUtils.d("11-childCount=" + childCount + "==spanCount=" + spanCount + "==childCount2==" + childCount2);

        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();

            if (headCount > i) {//有头文件时不绘制
            } else {
                if (footCount != 0 && i >= childCount - footCount) {//此时不绘制尾部
                } else {
                    //头部布局的高度，
                    int headHight = 0;
                    //说明：当item的总个数（除了头尾布局）正好除以每行的个数，此时即需要设置高度
                    if ((childCount2 - footCount - headCount) % spanCount != 0) {
                        for (int j = 0; j < headCount; j++) {//这是在头部高度相等时可以（*headCount），否则则for循环相加
                            headHight += parent.getChildAt(j).getBottom() - parent.getChildAt(j).getTop();
                        }
                    }
                    if (i == headCount) {//添加最左侧的一个竖条
                    LogUtils.d("==for循环参数===" + childCount + "===" + headCount + "--" + footCount + "==" + childCount2);
                    for (int j = 0; j <= childCount2 - footCount - headCount; j++) {
                        LogUtils.d("=垂直时绘制图形=" + j);
                        //这是每行的第一个得绘制
                        if ((j) % spanCount == 0) {//说明是列表第一个（除去头）
                            LogUtils.d("=垂直时绘制图形0===" + j);
                            View child2 = parent.getChildAt(j);
                            if (child2 == null) {
                                LogUtils.d("==怎么会空呢==" + j);
                                break;
                            }
                            RecyclerView.LayoutParams params2 = (RecyclerView.LayoutParams) child2
                                    .getLayoutParams();
                            //说明：params2.leftMargin是指子布局中的margin-left的大小；
                            //说明：child2.getLeft()其实就是子布局离left的距离加上margin离左边的距离
                            //说明：当有头文件时，需要加上头文件的高度
                            final int top0 = child2.getTop() - params2.topMargin + headHight;// - mDivider.getIntrinsicWidth()
                            final int bottom0 = child2.getBottom() + params2.bottomMargin + mDivider.getIntrinsicWidth() + headHight;
                            final int left0 = 0;//child2.getLeft() - params2.leftMargin
                            final int right0 = left0 + mDivider.getIntrinsicWidth();

                            LogUtils.d("==for循环参数===垂直时绘制图形==每行最左侧==" + j + "--" + headCount + "---" + spanCount
                                    + "===" + left0 + "===" + top0 + "===" + right0 + "===" + bottom0
                                    + "==child=" + child2.getLeft() + "===" + child2.getTop() + "===" + child2.getBottom()
                                    + "===" + child2.getRight() + "==params==" + params2.leftMargin + "====" + params2.rightMargin
                                    + "====" + params2.topMargin + "====" + params2.bottomMargin + "---tophead--" + parent.getChildAt(0).getBottom()
                                    + "====" + parent.getChildAt(0).getTop());
                            mDivider.setBounds(left0, top0, right0, bottom0);
                            mDivider.draw(c);
                        }
                    }
                    }
                    if (i == headCount) {//正常的垂直页面
                        for (int x = 0; x <= childCount2 - footCount - headCount; x++) {
                            View child3 = parent.getChildAt(x);
                            if (child3 == null) {
                                LogUtils.d("==怎么会空呢==" + x);
                                break;
                            }
                            RecyclerView.LayoutParams params3 = (RecyclerView.LayoutParams) child3
                                    .getLayoutParams();
                            final int top = child3.getTop() - params3.topMargin - mDivider.getIntrinsicWidth();
                            final int bottom = child3.getBottom() + params3.bottomMargin;
                            final int left = child3.getRight() + params3.rightMargin - mDivider.getIntrinsicWidth();
                            final int right = left + mDivider.getIntrinsicWidth();

                            LogUtils.d("垂直时绘制图形====" + i + "===" + left + "===" + top + "===" + right + "===" + bottom);
                            mDivider.setBounds(left, top, right, bottom);
                            mDivider.draw(c);
                        }
                    }
                }
            }
        }
    }

}