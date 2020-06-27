package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallback {

    /**
     * 淘口令加载结果
     *
     * @param photoUrl
     * @param ticketResult
     */
    void onTicketCallback(String photoUrl, TicketResult ticketResult);
}
