package com.longrise.ticketunion.presenter.impl;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.Categorys;
import com.longrise.ticketunion.presenter.IHomePresenter;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mHomeCallback;

    @Override
    public void getCategorys() {
        if (mHomeCallback != null) {
            mHomeCallback.onLoading();
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categorys> task =  api.getCategorys();
        task.enqueue(new Callback<Categorys>() {
            @Override
            public void onResponse(Call<Categorys> call, Response<Categorys> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Categorys categorys = response.body();
                    if (mHomeCallback != null) {
                        if (categorys == null || categorys.getData().size() == 0) {
                            mHomeCallback.onEmpty();
                        } else {
                            mHomeCallback.onCategoryCallback(categorys);
                        }
                    }
                } else {
                    if (mHomeCallback != null) {
                        mHomeCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categorys> call, Throwable t) {
                if (mHomeCallback != null) {
                    mHomeCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        mHomeCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mHomeCallback = null;
    }
}
