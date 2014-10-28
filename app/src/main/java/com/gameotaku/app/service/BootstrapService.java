package com.gameotaku.app.service;

import com.gameotaku.app.core.CheckIn;
import com.gameotaku.app.core.News;
import com.gameotaku.app.core.User;
import com.gameotaku.app.db.dao.WebGame;
import com.gameotaku.app.wrapper.WebGamesWrapper;

import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private WebGameService getWebGameService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .build();
        return restAdapter.create(WebGameService.class);
    }

    private NewsService getNewsService() {
        return getRestAdapter().create(NewsService.class);
    }

    private CheckInService getCheckInService() {
        return getRestAdapter().create(CheckInService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public List<WebGame> getWebGames(Map<String, String> options) {
        /*
        getWebGameService().getWebGameList(options, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //Try to get response body
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String result = sb.toString();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        */
        WebGamesWrapper wrapper = getWebGameService().getWebGames(options);
        return getWebGameService().getWebGames(options).getProjects();
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */
    public List<News> getNews() {
        return getNewsService().getNews().getResults();
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public List<User> getUsers() {
//        UsersWrapper users = getUserService().getUsers();
//        List<User> list = users.getResults();
//        getUserService().getUserList(new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//                //Try to get response body
//                BufferedReader reader = null;
//                StringBuilder sb = new StringBuilder();
//                try {
//
//                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
//
//                    String line;
//
//                    try {
//                        while ((line = reader.readLine()) != null) {
//                            sb.append(line);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                String result = sb.toString();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
        return getUserService().getUsers().getResults();
    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     */
    public List<CheckIn> getCheckIns() {
        return getCheckInService().getCheckIns().getResults();
    }

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }
}