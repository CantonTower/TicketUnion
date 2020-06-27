package com.longrise.ticketunion.model.domain;

/**
 * 点击商品进入淘口令界面的数据接口
 */

public interface IBaseInfo {

    /**
     * 商品的图片
     */
    String getTicketPhoto();

    /**
     * 商品的标题
     */
    String getTicketTitle();

    /**
     * 商品的url
     */
    String getTicketUrl();

}
