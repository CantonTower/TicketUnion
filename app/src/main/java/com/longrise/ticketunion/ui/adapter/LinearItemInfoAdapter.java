package com.longrise.ticketunion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.model.domain.HomePagerContent;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.ILinearItemInfo;
import com.longrise.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearItemInfoAdapter extends RecyclerView.Adapter<LinearItemInfoAdapter.InnerHolder> {
    private List<ILinearItemInfo> mListBean = new ArrayList<>();
    private OnListItemClickListener mItemClickListener;

    @NonNull
    @Override
    public LinearItemInfoAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mListBean.clear();
        mListBean.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        int olderSize = mListBean.size();
        mListBean.addAll(contents);
        // 只更新新添加的数据
        notifyItemRangeChanged(olderSize, contents.size());
    }

    @Override
    public void onBindViewHolder(@NonNull LinearItemInfoAdapter.InnerHolder holder, int position) {
        Context context = holder.itemView.getContext();
        ILinearItemInfo bean = mListBean.get(position);

        holder.tvGoodsTitle.setText(bean.getTicketTitle());
        String originalPrise = bean.getFinalPrise();
        int savePrise = (int) bean.getCouponAmount(); // 节省的钱数
        float finalPrise = Float.parseFloat(originalPrise) - savePrise;
        holder.tvGoodsSavePrise.setText(String.format(context.getString(R.string.text_goods_save_prise), savePrise));
        holder.tvGoodsFinalPrise.setText(String.format("%.2f", finalPrise));
        holder.tvGoodsOriginalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvGoodsOriginalPrise.setText(String.format(context.getString(R.string.text_goods_original_prise), originalPrise));
        holder.tvGoodsSellCount.setText(String.format(context.getString(R.string.text_goods_sell_count), bean.getVolume()));

        ViewGroup.LayoutParams layoutParams = holder.ivGoodsPhoto.getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;
        int ivSize = (width > height ? width : height) / 2;
        Glide.with(context).load(UrlUtils.getSizePhotoPath(bean.getTicketPhoto(), ivSize)).into(holder.ivGoodsPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBean.size();
    }

    class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView ivGoodsPhoto; // 照片
        private TextView tvGoodsTitle; // 标题
        private TextView tvGoodsSavePrise; // 节省的钱数
        private TextView tvGoodsFinalPrise; // 最终的价格
        private TextView tvGoodsOriginalPrise; // 原来的价格
        private TextView tvGoodsSellCount; // 销售量

        private InnerHolder(@NonNull View itemView) {
            super(itemView);
            tvGoodsTitle = itemView.findViewById(R.id.tv_goods_title);
            ivGoodsPhoto = itemView.findViewById(R.id.iv_goods_photo);
            tvGoodsSavePrise = itemView.findViewById(R.id.tv_goods_save_prise);
            tvGoodsFinalPrise = itemView.findViewById(R.id.tv_goods_final_prise);
            tvGoodsOriginalPrise = itemView.findViewById(R.id.tv_goods_original_prise);
            tvGoodsSellCount = itemView.findViewById(R.id.tv_goods_sell_count);
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(IBaseInfo itemData);
    }
}
