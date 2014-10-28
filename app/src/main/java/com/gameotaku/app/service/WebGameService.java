package com.gameotaku.app.service;

import com.gameotaku.app.wrapper.WebGamesWrapper;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Vincent on 8/31/14.
 */
public interface WebGameService {
    @GET(Constants.Http.URL_WEBGAMES_FRAG)
    WebGamesWrapper getWebGames(@QueryMap Map<String, String> options);

    @GET(Constants.Http.URL_WEBGAMES_FRAG)
    void getWebGameList(@QueryMap Map<String, String> options, Callback<Response> callback);
}
