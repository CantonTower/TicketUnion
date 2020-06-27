package com.longrise.ticketunion.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.SellContent;
import com.longrise.ticketunion.presenter.ISellPagePresenter;
import com.longrise.ticketunion.presenter.ITicketPagerPresenter;
import com.longrise.ticketunion.ui.activity.TicketActivity;
import com.longrise.ticketunion.ui.adapter.SellAdapter;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.utils.TicketUtil;
import com.longrise.ticketunion.view.ISellPageCallback;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellFragment extends BaseFragment implements ISellPageCallback {
    private ISellPagePresenter mSellPresenter;
    private TextView mtvBaseTitle;
    private RecyclerView mSellList;
    private SellAdapter mSellAdapter;
    private TwinklingRefreshLayout mSellRefreshLayout;

    private static final int DEFAULT_SPAN_COUNT = 2;

    @Override
    public int getRootViewResId() {
        return R.layout.fragment_sell;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_title_fragment_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
        mtvBaseTitle = rootView.findViewById(R.id.tv_base_title);
        mtvBaseTitle.setText("特惠宝贝");
        mSellList = rootView.findViewById(R.id.rv_sell_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        mSellList.setLayoutManager(gridLayoutManager);
        mSellAdapter = new SellAdapter();
        mSellList.setAdapter(mSellAdapter);

        mSellRefreshLayout = rootView.findViewById(R.id.sell_refresh_layout);
        mSellRefreshLayout.setEnableLoadmore(true);
        mSellRefreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSellRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mSellPresenter.loadMoreContent();
            }
        });

        mSellAdapter.setOnSellItemClickListener(new SellAdapter.OnSellItemClickListener() {
            @Override
            public void onSellItemClick(IBaseInfo bean) {
                TicketUtil.toTicketPage(getContext(), bean);
            }
        });
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSellPresenter = PresenterManager.getInstance().getSellPagePresenter();
        mSellPresenter.registerViewCallback(this);
        mSellPresenter.getSellContent();
    }

    @Override
    protected void release() {
        super.release();
        if (mSellPresenter != null) {
            mSellPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onContentCallback(SellContent result) {
        setUpState(State.SUCCESS);
        mSellAdapter.setData(result);
    }

    @Override
    public void onLoadMoreContentCallback(SellContent moreResult) {
        mSellRefreshLayout.finishLoadmore();
        mSellAdapter.addData(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        mSellRefreshLayout.finishLoadmore();
        Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoreLoadedEmpty() {
        mSellRefreshLayout.finishLoadmore();
        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }
}
