package com.longrise.ticketunion.presenter.impl;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.TicketParams;
import com.longrise.ticketunion.model.domain.TicketResult;
import com.longrise.ticketunion.presenter.ITicketPagerPresenter;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.utils.UrlUtils;
import com.longrise.ticketunion.view.ITicketPagerCallback;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPagerPresenterImpl implements ITicketPagerPresenter {
    private ITicketPagerCallback mViewCallback;
    private String mPhotoUrl;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING,SUCCESS,ERROR, NONE,
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicketData(String title, String ticketUrl, String photoUrl) {
        onTicketLoading();
        mPhotoUrl = photoUrl;
        // 获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String targetUrl = UrlUtils.getTicketUrl(ticketUrl);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    onTicketLoadedSuccess();
                } else {
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) { // 已注册，已加载成功
            mViewCallback.onTicketCallback(mPhotoUrl, mTicketResult);
        } else { // 未注册，已加载成功
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) { // 已注册，网络请求失败
            mViewCallback.onError();
        } else { // 未注册，网络请求失败
            mCurrentState = LoadState.ERROR;
        }
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        mViewCallback = callback;
        if (mCurrentState != LoadState.NONE) { // 说明状态已经改变，网络请求已完成
            // 更新UI
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        mViewCallback = null;
    }
}
