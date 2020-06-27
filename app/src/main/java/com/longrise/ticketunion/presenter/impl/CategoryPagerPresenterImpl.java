package com.longrise.ticketunion.presenter.impl;

import android.util.Log;

import com.longrise.ticketunion.model.Api;
import com.longrise.ticketunion.model.domain.HomePagerContent;
import com.longrise.ticketunion.presenter.ICategoryPagerPresenter;
import com.longrise.ticketunion.utils.RetrofitManager;
import com.longrise.ticketunion.utils.UrlUtils;
import com.longrise.ticketunion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {
    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    private Map<Integer, Integer> mPagesInfo = new HashMap<>();
    private static final int DEFAULT_PAGE = 1;

    public CategoryPagerPresenterImpl() {

    }

    private Call<HomePagerContent> createTask(int categoryId, int targetPage) {
        // 根据分类ID去加载内容
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePageUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        return api.getHomePagerContent(homePageUrl);
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                callback.onLoading();
            }
        }
        Integer targetPage = mPagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            mPagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    HomePagerContent contents = response.body();
                    handleHomePageContentResult(contents, categoryId);
                } else {
                    handlerNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handlerNetworkError(categoryId);
            }
        });
    }

    // 通知UI层更新数据
    private void handleHomePageContentResult(HomePagerContent contents, int categoryId) {
        if (contents == null) {
            return;
        }
        List<HomePagerContent.DataBean> data = contents.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                if (data == null || contents.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(0, 5);
                    callback.onLooperListCallback(looperData);
                    callback.onContentCallback(data);
                }
            }
        }
    }

    private void handlerNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                callback.onError();
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        int currentPage = mPagesInfo.get(categoryId);
        currentPage++;
        final int transValue = currentPage;
        Call<HomePagerContent> task = createTask(categoryId, currentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    HomePagerContent contents = response.body();
                    handleLoadMoreResult(contents, categoryId);
                } else {
                    handleLoadMoreError(categoryId, transValue);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleLoadMoreError(categoryId, transValue);
            }
        });
    }

    private void handleLoadMoreResult(HomePagerContent contents, int categoryId) {
        if (contents == null) {
            return;
        }
        List<HomePagerContent.DataBean> datas = contents.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                if (datas == null || datas.size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreCallback(datas);
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId, int transValue) {
        int currentPage = transValue - 1;
        mPagesInfo.put(categoryId, currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
