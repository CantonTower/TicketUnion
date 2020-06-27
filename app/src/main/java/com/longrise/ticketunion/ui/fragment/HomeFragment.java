package com.longrise.ticketunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.longrise.ticketunion.MainActivity;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.model.domain.Categorys;
import com.longrise.ticketunion.presenter.IHomePresenter;
import com.longrise.ticketunion.presenter.impl.HomePresenterImpl;
import com.longrise.ticketunion.ui.adapter.HomePagerAdapter;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.view.IHomeCallback;
import com.longrise.ticketunion.view.IMainActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends BaseFragment implements IHomeCallback {
    private TabLayout mHomeIndicator;
    private ViewPager mHomePager;
    private HomePagerAdapter mHomePagerAdapter;
    private EditText metSearchInput;

    private IHomePresenter mHomePresenter;

    @Override
    public int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_search_fragment_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mHomeIndicator = rootView.findViewById(R.id.home_indicator);
        mHomePager = rootView.findViewById(R.id.home_pager);
        metSearchInput = rootView.findViewById(R.id.et_search_input);
        mHomeIndicator.setupWithViewPager(mHomePager);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        metSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2SearchFragment();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategorys();
        }
    }

    @Override
    protected void onRetryClick() {
        if (mHomePresenter!=null) {
            mHomePresenter.getCategorys();
        }
    }

    @Override
    protected void release() {
        // 取消注册
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onCategoryCallback(Categorys categorys) {
        setUpState(State.SUCCESS);
        mHomePagerAdapter.setCategories(categorys);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }
}
