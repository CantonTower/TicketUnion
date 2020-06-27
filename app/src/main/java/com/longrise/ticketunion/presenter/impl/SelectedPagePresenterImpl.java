package com.longrise.ticketunion.presenter.impl;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.SelectedContent;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;
import com.longrise.ticketunion.presenter.ISelectedPagePresenter;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.utils.UrlUtils;
import com.longrise.ticketunion.view.ISelectedPageCallback;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {
    private ISelectedPageCallback mSelectedPageCallback;
    private Api mApi;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mSelectedPageCallback != null) {
            mSelectedPageCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    SelectedPageCategory categories = response.body();
                    if (categories != null) {
                        // 数据获取成功
                        if (mSelectedPageCallback != null) {
                            mSelectedPageCallback.onCategoriesCallback(categories);
                        }
                    } else {
                        // 数据获取为空
                        if (mSelectedPageCallback != null) {
                            mSelectedPageCallback.onEmpty();
                        }
                    }
                } else {
                    // 网络错误
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                // 网络错误
                onLoadError();
            }
        });
    }

    private void onLoadError() {
        if (mSelectedPageCallback != null) {
            mSelectedPageCallback.onError();
        }
    }

    private void onLoadEmpty() {
        if (mSelectedPageCallback != null) {
            mSelectedPageCallback.onEmpty();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        String targetUrl = UrlUtils.getSelectedPageContentUrl(item.getFavorites_id());
        Call<SelectedContent> task = mApi.getSelectedContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    SelectedContent content = response.body();
                    if (content != null) {
                        if (mSelectedPageCallback != null) {
                            mSelectedPageCallback.onContentCallback(content);
                        }
                    } else {
                        onLoadEmpty();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadError();
            }
        });
    }

    @Override
    public void reloadContent() {
        getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        mSelectedPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        mSelectedPageCallback = null;
    }
}
