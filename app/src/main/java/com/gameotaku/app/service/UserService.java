package com.gameotaku.app.service;

import com.gameotaku.app.core.Constants;
import com.gameotaku.app.core.User;
import com.gameotaku.app.wrapper.UsersWrapper;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface UserService {

    @GET(Constants.Http.URL_USERS_FRAG)
    UsersWrapper getUsers();

    @GET(Constants.Http.URL_USERS_FRAG)
    void getUserList(Callback<Response> callback);

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param email    The users email
     * @param password The users password
     * @return A login response.
     */
    @GET(Constants.Http.URL_AUTH_FRAG)
    User authenticate(@Query(Constants.Http.PARAM_USERNAME) String email,
                      @Query(Constants.Http.PARAM_PASSWORD) String password);
}
