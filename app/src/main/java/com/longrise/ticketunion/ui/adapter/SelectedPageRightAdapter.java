package com.longrise.ticketunion.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.SelectedContent;
import com.longrise.ticketunion.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedPageRightAdapter extends RecyclerView.Adapter<SelectedPageRightAdapter.InnerHolder> {
    private List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mDataList = new ArrayList<>();
    private OnSelectedPageRightItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_right, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData = mDataList.get(position);
        holder.tvTitle.setText(itemData.getTitle());
        if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
            holder.tvOriginalPrise.setText("晚了，没有优惠券了");
            holder.tvClickBuy.setVisibility(View.GONE);
        } else {
            holder.tvOriginalPrise.setText("原价：" + itemData.getZk_final_price());
            holder.tvClickBuy.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(itemData.getCoupon_info())) {
            holder.tvOffPrise.setVisibility(View.GONE);
        } else {
            holder.tvOffPrise.setVisibility(View.VISIBLE);
            holder.tvOffPrise.setText(itemData.getCoupon_info());
        }
        Glide.with(holder.itemView.getContext()).load(itemData.getPict_url()).into(holder.ivPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRightItemClick(itemData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode() == Constants.SUCCESS_CODE) {
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> data = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            mDataList.clear();
            mDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvOffPrise;
        private TextView tvTitle;
        private TextView tvClickBuy;
        private TextView tvOriginalPrise;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_select_right_photo);
            tvOffPrise = itemView.findViewById(R.id.tv_select_right_off_prise);
            tvTitle = itemView.findViewById(R.id.tv_select_right_title);
            tvClickBuy = itemView.findViewById(R.id.tv_select_right_click_buy);
            tvOriginalPrise = itemView.findViewById(R.id.tv_select_right_original_prise);
        }
    }

    public void setOnSelectedPageRightItemClickListener(OnSelectedPageRightItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnSelectedPageRightItemClickListener {
        void onRightItemClick(IBaseInfo itemBean);
    }
}
