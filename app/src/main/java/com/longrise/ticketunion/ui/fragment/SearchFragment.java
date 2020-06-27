package com.longrise.ticketunion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.model.domain.SearchRecommend;
import com.longrise.ticketunion.model.domain.SearchResult;
import com.longrise.ticketunion.presenter.ISearchPresenter;
import com.longrise.ticketunion.ui.adapter.LinearItemInfoAdapter;
import com.longrise.ticketunion.ui.custom.TextFlowLayout;
import com.longrise.ticketunion.utils.KeyboardUtil;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.utils.TicketUtil;
import com.longrise.ticketunion.view.ISearchViewCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends BaseFragment implements ISearchViewCallback,
        TextFlowLayout.OnFlowTextItemClickListener {
    private EditText mEtSearchInput;
    private ImageView mivDeleteSearchInput;
    private ISearchPresenter mSearchPresenter;
    private LinearLayout mLayoutHistory;
    private LinearLayout mLayoutRecommend;
    private TextFlowLayout mHistoriesView;
    private TextFlowLayout mRecommendView;
    private ImageView mivSearchHistoryDelete;
    private RecyclerView mSearchResultList;
    private LinearItemInfoAdapter mSearchResultAdapter;
    private TwinklingRefreshLayout mSearchRefreshLayout;
    private TextView mtvSearchBtn;

    @Override
    public int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_search_fragment_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
        mLayoutHistory = rootView.findViewById(R.id.layout_search_history);
        mLayoutRecommend = rootView.findViewById(R.id.layout_search_recommend);
        mHistoriesView = rootView.findViewById(R.id.search_history_view);
        mRecommendView = rootView.findViewById(R.id.search_recommend_view);
        mivSearchHistoryDelete = rootView.findViewById(R.id.iv_search_history_delete);
        mSearchResultList = rootView.findViewById(R.id.search_result_list);
        mSearchRefreshLayout = rootView.findViewById(R.id.search_refresh_layout);
        mEtSearchInput = rootView.findViewById(R.id.et_search_input);
        mivDeleteSearchInput = rootView.findViewById(R.id.iv_delete_search_input);
        mtvSearchBtn = rootView.findViewById(R.id.tv_search_btn);

        mSearchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        mSearchResultAdapter = new LinearItemInfoAdapter();
        mSearchResultList.setAdapter(mSearchResultAdapter);

        mSearchRefreshLayout.setEnableRefresh(false);
        mSearchRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
            mSearchPresenter.getReCommendWords();
        }
    }

    @Override
    protected void initListener() {
        mHistoriesView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);

        mivSearchHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.delHistories();
                }
            }
        });

        mSearchResultAdapter.setOnListItemClickListener(new LinearItemInfoAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo itemData) {
                TicketUtil.toTicketPage(getContext(), itemData);
            }
        });

        mSearchRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });

        mEtSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 点击键盘上的搜索按钮，发起搜索
                    String textStr = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(textStr) && mSearchPresenter != null) {
                        toSearch(textStr);
                    }
                }
                return false;
            }
        });

        mEtSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mivDeleteSearchInput.setVisibility(s.toString().length() > 0 ? View.VISIBLE : View.GONE);
                mtvSearchBtn.setText(isCanSearch() ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mivDeleteSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearchInput.setText("");
                switch2SearchHistoryPage();
            }
        });

        mtvSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanSearch() && mSearchPresenter != null) {
                    toSearch(mEtSearchInput.getText().toString().trim());
                    KeyboardUtil.hide(getContext(), v);
                } else {
                    KeyboardUtil.hide(getContext(), v);
                }
            }
        });
    }

    // 跳转至历史记录页面
    private void switch2SearchHistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        mLayoutRecommend.setVisibility(mRecommendView.getTextFlowSize() > 0 ? View.VISIBLE : View.GONE);
        mSearchRefreshLayout.setVisibility(View.GONE);
    }

    private boolean isCanSearch() {
        return mEtSearchInput.getText().toString().trim().length() > 0;
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onHistoriesLoaded(List<String> histories) {
        if (histories != null && histories.size() > 0) {
            mHistoriesView.setTextList(histories);
            mLayoutHistory.setVisibility(View.VISIBLE);
        } else {
            mLayoutHistory.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHistoriesDeleted() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        mSearchRefreshLayout.setVisibility(View.VISIBLE);
        mLayoutHistory.setVisibility(View.GONE);
        mLayoutRecommend.setVisibility(View.GONE);
        mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onLoadedMoreSuccess(SearchResult result) {
        mSearchRefreshLayout.finishLoadmore();
        mSearchResultAdapter.addData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onLoadedMoreEmpty() {
        mSearchRefreshLayout.finishLoadmore();
        Toast.makeText(getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadedMoreError() {
        mSearchRefreshLayout.finishLoadmore();
        Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        List<String> recommends = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            recommends.add(item.getKeyword());
        }
        if (recommends.size() > 0) {
            mLayoutRecommend.setVisibility(View.VISIBLE);
            mRecommendView.setTextList(recommends);
        } else {
            mLayoutRecommend.setVisibility(View.GONE);
        }
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

    private void toSearch(String searchText) {
        if (mSearchPresenter != null) {
            mSearchResultList.scrollToPosition(0);
            mSearchPresenter.doSearch(searchText);
            mEtSearchInput.setText(searchText);
            mEtSearchInput.requestFocus();
            mEtSearchInput.setSelection(searchText.length()); // 将光标放在搜索关键字之后(需获取焦点)
        }
    }

    @Override
    public void onFlowTextItemClick(String itemStr) {
        toSearch(itemStr);
    }
}
