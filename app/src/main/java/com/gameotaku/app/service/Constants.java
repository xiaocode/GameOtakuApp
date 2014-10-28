package com.gameotaku.app.service;

/**
 * Created by Vincent on 8/31/14.
 */
public final class Constants {
    private Constants() {
    }

    public static final class LogTag {
        public static final String GAMEMANAGER_TAG = "GameManager";
        public static final String LOADER_TAG = "Loader";

        private LogTag() {
        }
    }

    public static final class Intent {
        public static final String BROWSER_ITEM_ID = "browse_item_id";
        public static final String BROWSER_ITEM_NAME = "browse_item_name";
        public static final String BROWSER_ITEM_URL = "browse_item_url";
        public static final String BROWSER_ITEM_ICON = "browse_item_icon";

        private Intent() {
        }
    }

    public static final class SharedPreference {
        public static final String UPDATE_TIME = "sp_update_time";
        public static final String FORCE_UPDATE = "sp_force_update";

        private SharedPreference() {
        }
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://duduluwan.duapp.com";
        /**
         * Authentication URL
         */
        public static final String URL_AUTH_FRAG = "/1/login";
        public static final String URL_AUTH = URL_BASE + URL_AUTH_FRAG;
        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG = "/1/users";
        public static final String URL_USERS = URL_BASE + URL_USERS_FRAG;
        /**
         * List WebGames URL
         */
        public static final String URL_WEBGAMES_FRAG = "/1/webgames.php";
        public static final String URL_WEBGAMES = URL_BASE + URL_WEBGAMES_FRAG;
        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";
        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";

        private Http() {
        }


    }
}
