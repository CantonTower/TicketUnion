package com.longrise.ticketunion.presenter.impl;

import android.text.TextUtils;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.Histories;
import com.longrise.ticketunion.model.domain.SearchRecommend;
import com.longrise.ticketunion.model.domain.SearchResult;
import com.longrise.ticketunion.presenter.ISearchPresenter;
import com.longrise.ticketunion.utils.Constants;
import com.longrise.ticketunion.utils.JsonCacheUtil;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.view.ISearchViewCallback;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {
    private Api mApi;
    private ISearchViewCallback mSearchViewCallback;
    private JsonCacheUtil mJsonCacheUtil;

    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = "";

    public SearchPresenterImpl() {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        Retrofit retrofit = retrofitManager.getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (histories != null) {
            List<String> historyList = histories.getHistories();
            if (historyList != null && historyList.size() > 0) {
                if (mSearchViewCallback != null) {
                    mSearchViewCallback.onHistoriesLoaded(historyList);
                }
            }
        } else {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onHistoriesLoaded(null);
            }
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoriesDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";

    public static final int DEFAULT_HISTORIES_SIZE = 10;

    /**
     * 添加历史记录
     */
    private void saveHistories(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historyList = null;
        if (histories != null) {
            historyList = histories.getHistories();
        } else {
            histories = new Histories();
        }
        if (historyList != null) {
            // 当包含history时，移除后重新添加，会将history的位置放在最后
            if (historyList.contains(history)) {
                historyList.remove(history);
            }
        } else {
            historyList = new ArrayList<>();
        }
        if (histories.getHistories() != historyList) {
            histories.setHistories(historyList);
        }
        if (historyList.size() >= DEFAULT_HISTORIES_SIZE) {
            // 保留List前面的MAX-1项
            // 从MAX-1项开始，后面的项全部删除
            historyList = historyList.subList(0, DEFAULT_HISTORIES_SIZE - 1);
            histories.setHistories(historyList);
        }
        historyList.add(0, history);
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (keyword != null && !keyword.equals(mCurrentKeyword)) {
            mCurrentKeyword = keyword;
            saveHistories(keyword);
        }
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    if (isResultEmpty(response.body())) {
                        if (mSearchViewCallback != null) {
                            mSearchViewCallback.onEmpty();
                        }
                    } else {
                        if (mSearchViewCallback != null) {
                            mSearchViewCallback.onSearchSuccess(response.body());
                        }
                    }
                } else {
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                if (mSearchViewCallback != null) {
                    mSearchViewCallback.onError();
                }
            }
        });
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
            if (data != null && data.size() > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void research() {
        if (TextUtils.isEmpty(mCurrentKeyword)) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            mCurrentPage = DEFAULT_PAGE;
            doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        if (TextUtils.isEmpty(mCurrentKeyword)) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onLoadedMoreEmpty();
            }
        } else {
            mCurrentPage++;
            Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
            task.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    if (response.code() == HttpsURLConnection.HTTP_OK) {
                        if (isResultEmpty(response.body())) {
                            mCurrentPage--;
                            if (mSearchViewCallback != null) {
                                mSearchViewCallback.onLoadedMoreEmpty();
                            }
                        } else {
                            if (mSearchViewCallback != null) {
                                mSearchViewCallback.onLoadedMoreSuccess(response.body());
                            }
                        }
                    } else {
                        mCurrentPage--;
                        if (mSearchViewCallback != null) {
                            mSearchViewCallback.onLoadedMoreError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    mCurrentPage--;
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onLoadedMoreError();
                    }
                }
            });
        }
    }

    @Override
    public void getReCommendWords() {
        Call<SearchRecommend> task = mApi.getSearchRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    List<SearchRecommend.DataBean> data = response.body().getData();
                    if (data != null && data.size() > 0) {
                        if (mSearchViewCallback != null) {
                            mSearchViewCallback.onRecommendWordsLoaded(data);
                        }
                    } else {
                        // 获取推荐字为空
                    }
                } else {
                    // 网络加载失败
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                // 网络加载失败
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchViewCallback callback) {
        mSearchViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchViewCallback callback) {
        mSearchViewCallback = null;
    }
}
