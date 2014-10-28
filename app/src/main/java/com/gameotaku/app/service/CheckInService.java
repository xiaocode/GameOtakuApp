package com.gameotaku.app.service;

import com.gameotaku.app.core.Constants;
import com.gameotaku.app.wrapper.CheckInWrapper;

import retrofit.http.GET;

public interface CheckInService {

    @GET(Constants.Http.URL_CHECKINS_FRAG)
    CheckInWrapper getCheckIns();
}
