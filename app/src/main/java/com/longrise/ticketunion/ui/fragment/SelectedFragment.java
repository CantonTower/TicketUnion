package com.longrise.ticketunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.SelectedContent;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;
import com.longrise.ticketunion.presenter.ISelectedPagePresenter;
import com.longrise.ticketunion.ui.adapter.SelectedPageLeftAdapter;
import com.longrise.ticketunion.ui.adapter.SelectedPageRightAdapter;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.utils.TicketUtil;
import com.longrise.ticketunion.view.ISelectedPageCallback;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback {
    private ISelectedPagePresenter mSelectedPresenter;
    private TextView mtvBaseTitle;
    private RecyclerView mrvLeftCategoryList;
    private RecyclerView mrvRightContentList;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageRightAdapter mRightAdapter;

    @Override
    public int getRootViewResId() {
        return R.layout.fragment_selected;
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
        mtvBaseTitle.setText("精选宝贝");
        mrvLeftCategoryList = rootView.findViewById(R.id.rv_left_category_list);
        mrvLeftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        mrvLeftCategoryList.setAdapter(mLeftAdapter);

        mrvRightContentList = rootView.findViewById(R.id.rv_right_content_list);
        mrvRightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageRightAdapter();
        mrvRightContentList.setAdapter(mRightAdapter);
    }

    @Override
    protected void initPresenter() {
        mSelectedPresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPresenter.registerViewCallback(this);
        mSelectedPresenter.getCategories();
    }

    @Override
    public void onCategoriesCallback(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
    }

    @Override
    public void onContentCallback(SelectedContent content) {
        mRightAdapter.setData(content);
        mrvRightContentList.scrollToPosition(0);
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
    protected void onRetryClick() {
        if (mSelectedPresenter != null) {
            mSelectedPresenter.reloadContent();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void initListener() {
        // 左边分类item的点击事件
        mLeftAdapter.setOnLeftItemClickListener(new SelectedPageLeftAdapter.OnLeftItemClickListener() {
            @Override
            public void onLeftItemClick(SelectedPageCategory.DataBean itemBean) {
                mSelectedPresenter.getContentByCategory(itemBean);
            }
        });

        mRightAdapter.setOnSelectedPageRightItemClickListener(new SelectedPageRightAdapter.OnSelectedPageRightItemClickListener() {
            @Override
            public void onRightItemClick(IBaseInfo itemBean) {
                TicketUtil.toTicketPage(getContext(), itemBean);
            }
        });
    }

    @Override
    protected void release() {
        if (mSelectedPresenter != null) {
            mSelectedPresenter.unregisterViewCallback(this);
        }
    }
}
