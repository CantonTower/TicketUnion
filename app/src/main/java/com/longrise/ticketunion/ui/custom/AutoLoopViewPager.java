package com.longrise.ticketunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.longrise.ticketunion.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class AutoLoopViewPager extends ViewPager {

    // 切换间隔时长，单位毫秒
    public static final long DEFAULT_DURATION = 2000;

    private long mDuration = DEFAULT_DURATION;


    private boolean mIsLoop = false;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //  获取自定义属性
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        // 获取属性
        // AutoLoopStyle_duration:资源文件_属性名(资源文件加下划线加属性名)
        mDuration = t.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        // 资源回收
        t.recycle();
    }

    /**
     * 设置切换时长
     *
     * @param duration 时长，单位:毫秒
     */
    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void startLoop() {
        mIsLoop = true;
        post(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (mIsLoop) {
                postDelayed(this, mDuration);
            }
        }
    };

    public void stopLoop() {
        mIsLoop = false;
        removeCallbacks(mTask);
    }
}
