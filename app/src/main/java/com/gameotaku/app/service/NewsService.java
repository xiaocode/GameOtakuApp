package com.gameotaku.app.service;

import com.gameotaku.app.core.Constants;
import com.gameotaku.app.wrapper.NewsWrapper;

import retrofit.http.GET;


/**
 * Interface for defining the news service to communicate with Parse.com
 */
public interface NewsService {

    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getNews();

}
