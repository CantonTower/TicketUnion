package com.longrise.ticketunion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.longrise.ticketunion.model.domain.HomePagerContent;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.ILinearItemInfo;
import com.longrise.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class LooperPagerAdapter extends PagerAdapter {
    private List<HomePagerContent.DataBean> mDataBeans = new ArrayList<>();
    private OnLooperPageItemClickListener mLooperItemClickListener;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 处理滑动越界问题
        int realPosition = position % mDataBeans.size();
        ILinearItemInfo bean = mDataBeans.get(realPosition);
        // 由于图片的布局是MATCH_PARENT(-1)，是不能拿到具体大小的，因此可以拿它的父容器的大小
        int width = container.getMeasuredWidth();
        int height = container.getMeasuredHeight();
        int ivSize = (width > height ? width : height) / 2;
        String photoUrl = UrlUtils.getSizePhotoPath(bean.getTicketPhoto(), ivSize);
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(photoUrl).into(iv);
        container.addView(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLooperItemClickListener != null) {
                    mLooperItemClickListener.onLooperItemClick(bean);
                }
            }
        });
        return iv;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return mDataBeans.size();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setLooperPageItemClickListener(OnLooperPageItemClickListener listener) {
        mLooperItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener {
        void onLooperItemClick(IBaseInfo itemData);
    }

}
