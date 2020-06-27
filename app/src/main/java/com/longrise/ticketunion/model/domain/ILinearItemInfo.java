package com.longrise.ticketunion.model.domain;

/**
 * 首页列表内容和搜索内容列表的数据接口
 */

public interface ILinearItemInfo extends IBaseInfo {

    /**
     * 获取商品原价
     */
    String getFinalPrise();

    /**
     * 获取优惠价格
     */
    long getCouponAmount();

    /**
     * 获取销量
     */
    int getVolume();
}
