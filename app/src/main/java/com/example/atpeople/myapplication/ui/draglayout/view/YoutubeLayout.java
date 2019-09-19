package com.example.atpeople.myapplication.ui.draglayout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.atpeople.myapplication.R;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class YoutubeLayout extends ViewGroup {
    private static final String TAG = "YoutubeLayout";
    private final ViewDragHelper mDragHelper;

    private View mHeaderView;
    private View mDescView;
    private float mInitialMotionX;
    private float mInitialMotionY;

    private int mDragRange;
    private int mTop;
    private float mDragOffset;

    public YoutubeLayout(Context context) {
        this(context, null);
    }

    public YoutubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        mHeaderView = findViewById(R.id.viewHeader);
        mDescView = findViewById(R.id.viewDesc);
    }

    public void maximize() {
        smoothSlideTo(0f);
    }

    public void minimize() {
        smoothSlideTo(1f);
    }

    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);
        // 设定子View滑动
        if (mDragHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            System.out.println("tryCaptureView1");
            return child == mHeaderView;

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            System.out.println("onViewPositionChanged3");
            mTop = top;
            mDragOffset = (float) top / mDragRange;
            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleX(1 - mDragOffset / 2);
            mHeaderView.setScaleY(1 - mDragOffset / 2);
            mDescView.setAlpha(1 - mDragOffset);
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            System.out.println("onViewReleased4");
            int top = getPaddingTop();
            System.out.println("444444:" + top);
            // 轴速度大于o，或者是滑动超过一半
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }
            // 設定位置
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            System.out.println("getViewVerticalDragRange5   " + mDragRange);
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            System.out.println("clampViewPositionVertical2");
            final int topBound = getPaddingTop();
            System.out.println("2222getPaddingTop: " + getPaddingTop());
            final int bottomBound = getHeight() - mHeaderView.getHeight()
                    - mHeaderView.getPaddingBottom();
            /**
             * Math.max(top, topBound):不得往上滑动超过顶部 Math.min(Math.max(top,
             * topBound), bottomBound);：不得滑动超过Header的高度
             **/
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

    }

    @Override
    public void computeScroll() {
        System.out.println("computeScrol " + getHeight());

        if (mDragHelper.continueSettling(true)) {
            // 接著显示下一帧
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.out.println("onInterceptTouchEvent");
        final int action = MotionEventCompat.getActionMasked(ev);
        // 子View不接受OnTouch事件
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // 这里的xy 是手指按下的时候，所在屏幕上的坐标
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x,
                        (int) y);
                System.out.println(x + " xxxxxxxxxxxx" + y + "  " + interceptTap);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                /* useless */
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    return false;
                }
            }

        }

        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        System.out.println("onTouchEvent");
        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();



        // 判斷点击事件是否在header上
        boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            // 手指按下的时候，记住在屏幕的坐标
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }
            // 手指离开屏幕后，拿到在屏幕的坐标，相减得到差值dx dy
            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        // 显示全部
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                }
                // 手指移动后的x坐标-手指开始触摸的X坐标<0,就是左滑，(坐标点在左上角，左往右和上往下，坐标会增加)
                // 监听手指滑动的方向
                if (dy > 0 && (Math.abs(dy) > 25)) {
                    Log.e(TAG, "向下"+(dy));
                    } else if (dy < 0 && (Math.abs(dy) > 25)) {
                    Log.e(TAG, "向上"+(dy));
                }
                // 只有在底部(起始y坐标>800, 上下滑动不超过100)，左滑距离超过25
                if (dx < 0 && (Math.abs(dx) > 25) && mInitialMotionY>800 && Math.abs(dy)<100) {
                    Log.e(TAG, "向左:");
                    // 左滑的时候，应该关掉悬浮
                    this.setVisibility(INVISIBLE);
                } else if (dx > 0 && (Math.abs(dx) > 25)) {
                    Log.e(TAG, "向右");
                }
                break;
            }
        }
        /**
         * 当点击与Header之下时，isHeaderViewUnder为假，直接判断触摸点是否在DescView之中，在就消费事件不在分发
         * 根据下方DescView是否可见判断是否让ListView获得焦点
         * */
        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y)
                || isViewHit(mDescView, (int) x, (int) y);
    }

    private boolean isViewHit(View view, int x, int y) {
        // x，y分别为相对于本身的坐标
        int[] viewLocation = new int[2];
        // 整個屏幕绝对坐标
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        // 相对于屏幕坐标
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0]
                && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1]
                // 在headerView内部
                && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());
        mDescView.layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);
    }
}
