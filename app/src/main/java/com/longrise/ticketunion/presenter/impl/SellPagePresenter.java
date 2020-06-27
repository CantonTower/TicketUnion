package com.longrise.ticketunion.presenter.impl;

import android.util.Log;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.SellContent;
import com.longrise.ticketunion.presenter.ISellPagePresenter;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.utils.UrlUtils;
import com.longrise.ticketunion.view.ISellPageCallback;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SellPagePresenter implements ISellPagePresenter {
    private ISellPageCallback mSellViewCallback;
    private Api mApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private boolean mIsLoading = false;

    public SellPagePresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (mSellViewCallback != null) {
            mSellViewCallback.onLoading();
        }
        String targetUrl = UrlUtils.getSellContentUrl(mCurrentPage);
        Call<SellContent> task = mApi.getSellContent(targetUrl);
        task.enqueue(new Callback<SellContent>() {
            @Override
            public void onResponse(Call<SellContent> call, Response<SellContent> response) {
                mIsLoading = false;
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    // 数据获取成功
                    SellContent body = response.body();
                    onSuccess(body);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SellContent> call, Throwable t) {
                onError();
            }
        });
    }

    @Override
    public void loadMoreContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        mCurrentPage++;
        String targetUrl = UrlUtils.getSellContentUrl(mCurrentPage);
        Call<SellContent> task = mApi.getSellContent(targetUrl);
        task.enqueue(new Callback<SellContent>() {
            @Override
            public void onResponse(Call<SellContent> call, Response<SellContent> response) {
                mIsLoading = false;
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    onLoadMoreSuccess(response.body());
                } else {
                    mCurrentPage--;
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SellContent> call, Throwable t) {
                mCurrentPage--;
                onLoadMoreError();
            }
        });
    }

    /**
     * 首页加载数据成功
     * @param body
     */
    private void onSuccess(SellContent body) {
        if (isEmpty(body)) {
            onEmpty();
        } else {
            if (mSellViewCallback != null) {
                mSellViewCallback.onContentCallback(body);
            }
        }
    }

    /**
     * 首页加载数据为空
     */
    private void onEmpty() {
        if (mSellViewCallback != null) {
            mSellViewCallback.onEmpty();
        }
    }

    /**
     * 首页加载数据失败
     */
    private void onError() {
        if (mSellViewCallback != null) {
            mSellViewCallback.onError();
        }
    }

    /**
     * 加载更过数据成功
     */
    private void onLoadMoreSuccess(SellContent body) {
        if (isEmpty(body)) {
            onLoadMoreEmpty();
        } else {
            if (mSellViewCallback != null) {
                mSellViewCallback.onLoadMoreContentCallback(body);
            }
        }
    }

    /**
     * 加载更过数据为空
     */
    private void onLoadMoreEmpty() {
        if (mSellViewCallback != null) {
            mSellViewCallback.onMoreLoadedEmpty();
        }
    }

    /**
     * 加载更多数据网络错误
     */
    private void onLoadMoreError() {
        if (mSellViewCallback != null) {
            mSellViewCallback.onMoreLoadedError();
        }
    }

    @Override
    public void reloadContent() {
        getSellContent();
    }

    /**
     * 判断body为空
     * @param body
     * @return
     */
    private boolean isEmpty(SellContent body) {
        try {
            List<SellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> resultList = body.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            if (resultList != null && resultList.size() > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void registerViewCallback(ISellPageCallback callback) {
        mSellViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISellPageCallback callback) {
        mSellViewCallback = null;
    }
}
