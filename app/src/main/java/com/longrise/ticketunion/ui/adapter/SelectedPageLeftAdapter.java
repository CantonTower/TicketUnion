package com.longrise.ticketunion.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longrise.ticketunion.R;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {
    private List<SelectedPageCategory.DataBean> mData = new ArrayList<>();
    private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        if (mCurrentSelectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeeee"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        SelectedPageCategory.DataBean dataBean = mData.get(position);
        holder.mtvCategoryLeft.setText(dataBean.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedPosition != position) {
                    mCurrentSelectedPosition = position;
                    notifyDataSetChanged();
                    if (mItemClickListener != null) {
                        mItemClickListener.onLeftItemClick(dataBean);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private TextView mtvCategoryLeft;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mtvCategoryLeft = itemView.findViewById(R.id.tv_category_left);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectedPageCategory.DataBean itemBean);
    }
}
