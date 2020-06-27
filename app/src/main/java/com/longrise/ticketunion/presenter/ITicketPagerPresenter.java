package com.longrise.ticketunion.presenter;

import com.longrise.ticketunion.base.IBasePresenter;
import com.longrise.ticketunion.view.ITicketPagerCallback;

public interface ITicketPagerPresenter extends IBasePresenter<ITicketPagerCallback> {

    /**
     * 生成淘口令
     * @param title
     * @param ticketUrl
     * @param photoUrl
     */
    void getTicketData(String title, String ticketUrl, String photoUrl);
}
