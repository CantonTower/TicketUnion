package com.longrise.ticketunion.model;

import com.longrise.ticketunion.model.domain.Categorys;
import com.longrise.ticketunion.model.domain.HomePagerContent;
import com.longrise.ticketunion.model.domain.SearchRecommend;
import com.longrise.ticketunion.model.domain.SearchResult;
import com.longrise.ticketunion.model.domain.SelectedContent;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;
import com.longrise.ticketunion.model.domain.SellContent;
import com.longrise.ticketunion.model.domain.TicketParams;
import com.longrise.ticketunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categorys> getCategorys();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedContent> getSelectedContent(@Url String url);

    @GET
    Call<SellContent> getSellContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getSearchRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);


}
