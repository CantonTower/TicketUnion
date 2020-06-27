package com.longrise.ticketunion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longrise.ticketunion.R;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {
    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth = 0;
    private int mItemHeight = 0;

    private List<String> mTextList = new ArrayList<>();
    private List<List<View>> lines = new ArrayList<>(); // 这个是描述所有的行

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 去拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = ta.getDimension(R.styleable.TextFlowLayout_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.TextFlowLayout_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        mTextList.clear();
        mTextList.addAll(textList);
        for (String text : mTextList) {
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            addView(item);

            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onFlowTextItemClick(text);
                    }
                }
            });
        }
    }

    public int getTextFlowSize() {
        if (mTextList != null) {
            return mTextList.size();
        }
        return 0;
    }

    /**
     * measureChild方法会运行子view的measure方法，measure方法会运行onMeasure方法。
     * 在onMeasure方法中会对子view的宽高mMeasuredWidth进行赋值。
     * 这也就是为什么measureChild方法运行之前子view的宽高是0，因为这个方法没有运行时，mMeasuredWidth是未赋值的
     */

    /**
     * 父容器运行measureChild方法，这个measureChild方法是viewGroup中的方法，也就是父容器测量子容器或者子view的大小
     * measureChild方法会运行onMeasure方法，这个onMeasure方法是view的方法，由于viewGroup继承view类，因此viewGroup也有onMeasure方法
     *
     * 父容器通过measureChild来测量子容器的大小，子容器此时会运行onMeasure方法，
     * 子容器在运行onMeasure方法时，又会运行measureChild去测量子容器的子容器的大小，
     * 直到最后的子容器是一个view，view一个容器，它里面不能再放view，因此它没有measureChild方法
     */

    /**
     * 在view类中measure方法是一个final方法，是不可以被重写的。
     * measure方法中会运行onMeasure方法，onMeasure可以被重写
     */

    /**
     * 当自定义view的宽高设置的是wrap_content时，此时必需要在onMeasure方法中setMeasuredDimension
     */

    /**
     * onMeasure方法和onLayout方法会执行多次
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        List<View> line = null;
        lines.clear();
        // 测量自己
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // 测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                // 如果某个子view不显示，就不需要测量它
                continue;
            }
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            // 测量后
            if (line == null) {
                // 说明当前行为空，可以添加进来
                line = createNewLine(itemView);
            } else {
                // 判断是否可以再继续添加
                if (canBeAdd(line, itemView)) {
                    // 可以添加，添加进去
                    line.add(itemView);
                } else {
                    // 不可以添加，新创建一行
                    line = createNewLine(itemView);
                }
            }
            mItemHeight = getChildAt(0).getMeasuredHeight();
            int selfHeight = (int) (mItemHeight * lines.size() + mItemVerticalSpace * (lines.size() + 1));
            setMeasuredDimension(mSelfWidth, selfHeight);
        }
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加新数据
     */
    private boolean canBeAdd(List<View> line, View itemView) {
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            totalWidth = totalWidth + itemView.getMeasuredWidth();
        }
        // 水平间距宽度
        totalWidth = (int) (totalWidth + mItemHorizontalSpace * (line.size() + 1));
        // 条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topOffSet = (int) mItemVerticalSpace;
        for (List<View> views : lines) {
            // views是每一行
            int leftOffSet = (int) mItemHorizontalSpace;
            for (View view : views) {
                // view是每一行中的每个item
                view.layout(leftOffSet, topOffSet, leftOffSet + view.getMeasuredWidth(), topOffSet + view.getMeasuredHeight());
                leftOffSet = leftOffSet + view.getMeasuredWidth() + (int) mItemHorizontalSpace;
            }
            topOffSet = (int) (topOffSet + mItemHeight + mItemVerticalSpace);
        }
    }

    public float getmItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setmItemHorizontalSpace(float mItemHorizontalSpace) {
        this.mItemHorizontalSpace = mItemHorizontalSpace;
    }

    public float getmItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setmItemVerticalSpace(float mItemVerticalSpace) {
        this.mItemVerticalSpace = mItemVerticalSpace;
    }

    private OnFlowTextItemClickListener mClickListener;

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        mClickListener = listener;
    }

    public interface OnFlowTextItemClickListener {
        void onFlowTextItemClick(String itemStr);
    }
}
