package com.longrise.ticketunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.longrise.ticketunion.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private FrameLayout mBaseContainer;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    private State mCurrentState = State.NONE;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY,
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStateView(inflater, container);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 获取最底层布局
     * @param inflater
     * @param container
     * @return
     */
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        // 加载成功的view
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);
        // 正在加载的view
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);
        // 错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);
        // 空页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        setUpState(State.NONE);
    }

    /**
     * 子类通过这个方法可切换页面状态
     * @param state
     */
    protected void setUpState(State state) {
        mCurrentState = state;
        mSuccessView.setVisibility(mCurrentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(mCurrentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    private View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getRootViewResId(), container, false);
    }

    private View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    protected void initView(View rootView) {
        mErrorView.findViewById(R.id.network_error).setOnClickListener(this);
    }

    /**
     * 如果子类需要去设置相关的事件，覆盖此方法
     */
    protected void initListener() {}

    protected void initPresenter() {}

    protected void loadData() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_error:
                onRetryClick();
                break;
        }
    }

    protected void onRetryClick() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    // 释放资源
    protected void release() {}

    /**
     * SUCCESS状态下的页面
     */
    protected abstract int getRootViewResId();
}
