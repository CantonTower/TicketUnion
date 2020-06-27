package com.longrise.ticketunion.ui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.model.domain.Categorys;
import com.longrise.ticketunion.model.domain.HomePagerContent;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.presenter.ICategoryPagerPresenter;
import com.longrise.ticketunion.ui.adapter.LinearItemInfoAdapter;
import com.longrise.ticketunion.ui.adapter.LooperPagerAdapter;
import com.longrise.ticketunion.ui.custom.AutoLoopViewPager;
import com.longrise.ticketunion.utils.Constants;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.utils.SizeUtils;
import com.longrise.ticketunion.utils.TicketUtil;
import com.longrise.ticketunion.view.ICategoryPagerCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback,
        LinearItemInfoAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {
    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private String mTitle;
    private int mMaterialId;
    private TextView mtvCurrentCategoryTitle;
    private RecyclerView mContentList;
    private LinearItemInfoAdapter mContentAdapter;
    private AutoLoopViewPager mLooperPager;
    private LooperPagerAdapter mLooperPagerAdapter;
    private LinearLayout mLooperPointContainer;
    private TwinklingRefreshLayout mRefreshLayout;

    public static HomePagerFragment newInstance(Categorys.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当fragment可见的时候，图片开始轮播
        mLooperPager.startLoop();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 当framgnet不可见的时候，图片停止轮播
        mLooperPager.stopLoop();
    }

    @Override
    protected void initView(View rootView) {
        mContentList = rootView.findViewById(R.id.home_pager_content_list);
        mLooperPager = rootView.findViewById(R.id.looper_pager);
        mtvCurrentCategoryTitle = rootView.findViewById(R.id.home_pager_title);
        mLooperPointContainer = rootView.findViewById(R.id.looper_point_container);
        mRefreshLayout = rootView.findViewById(R.id.layout_home_pager_refresh);

        // 设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置divide
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        // 创建适配器
        mContentAdapter = new LinearItemInfoAdapter();
        // 设置适配器
        mContentList.setAdapter(mContentAdapter);

        // 创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        // 设置适配器
        mLooperPager.setAdapter(mLooperPagerAdapter);

        // 设置refresh相关属性
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setLooperPageItemClickListener(this);

        mLooperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mCategoryPagerPresenter != null) {
                    mCategoryPagerPresenter.loadMore(mMaterialId);
                }
            }
        });
    }

    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < mLooperPointContainer.getChildCount(); i++) {
            View point = mLooperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(Constants.KEY_HOME_PAGER_TITLE);
            mMaterialId = bundle.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
            if (mCategoryPagerPresenter != null) {
                mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
            }
        }

        mtvCurrentCategoryTitle.setText(mTitle);
    }

    @Override
    public void onContentCallback(List<HomePagerContent.DataBean> contents) {
        setUpState(State.SUCCESS);
        mContentAdapter.setData(contents);
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

    @Override
    public void onLoadMoreCallback(List<HomePagerContent.DataBean> contents) {
        mContentAdapter.addData(contents);
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreError() {
        Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onLooperListCallback(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);
        mLooperPointContainer.removeAllViews();
        // 添加轮播图的指示点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.leftMargin = SizeUtils.dip2px(getContext(), 5);
            params.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(params);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            mLooperPointContainer.addView(point);
        }
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    /**
     * RecyclerView列表item的点击
     *
     * @param itemData 各item的数据
     */
    @Override
    public void onItemClick(IBaseInfo itemData) {
        jumpTicketActivity(itemData);
    }

    /**
     * Looper轮播图中item的点击
     * @param itemData 各图的数据
     */
    @Override
    public void onLooperItemClick(IBaseInfo itemData) {
        jumpTicketActivity(itemData);
    }

    private void jumpTicketActivity(IBaseInfo itemData) {
        TicketUtil.toTicketPage(getContext(), itemData);
    }
}
