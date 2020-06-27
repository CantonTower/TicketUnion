package com.longrise.ticketunion.base;

public interface IBasePresenter<T> {

    /**
     * 注册UI的通知接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消UI的通知接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
