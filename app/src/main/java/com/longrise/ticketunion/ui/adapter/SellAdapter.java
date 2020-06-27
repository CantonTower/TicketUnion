package com.longrise.ticketunion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.SellContent;
import com.longrise.ticketunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.InnerHolder> {
    private List<SellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mDataList = new ArrayList<>();
    private OnSellItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean = mDataList.get(position);
        String photoPath = UrlUtils.getPhotoPath(dataBean.getPict_url());
        Glide.with(holder.itemView.getContext()).load(photoPath).into(holder.ivSellPhoto);
        holder.tvSellTitle.setText(dataBean.getTitle());
        String originalPrise = dataBean.getZk_final_price();
        int couponPrise = dataBean.getCoupon_amount();
        float finalPrise = Float.parseFloat(originalPrise) - couponPrise;
        holder.tvSellOriginalPrise.setText("￥" + originalPrise);
        holder.tvSellOriginalPrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvSellOffPrise.setText(" 劵后价: " + String.format("%.2f", finalPrise));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onSellItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(SellContent result) {
        mDataList.clear();
        List<SellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> data = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(SellContent moreResult) {
        List<SellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> data = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        int oldDataSize = mDataList.size();
        mDataList.addAll(data);
        notifyItemRangeChanged(oldDataSize - 1, data.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView ivSellPhoto;
        private TextView tvSellTitle;
        private TextView tvSellOriginalPrise;
        private TextView tvSellOffPrise;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ivSellPhoto = itemView.findViewById(R.id.iv_sell_photo);
            tvSellTitle = itemView.findViewById(R.id.tv_sell_title);
            tvSellOriginalPrise = itemView.findViewById(R.id.tv_sell_original_prise);
            tvSellOffPrise = itemView.findViewById(R.id.tv_sell_off_prise);
        }
    }

    public void setOnSellItemClickListener(OnSellItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnSellItemClickListener {
        void onSellItemClick(IBaseInfo bean);
    }
}
