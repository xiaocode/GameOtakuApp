package com.gameotaku.app.browser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gameotaku.app.BootstrapApplication;
import com.gameotaku.app.ui.activity.BrowserActivity;
import com.gameotaku.app.util.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vincent on 9/6/14.
 */
public class JSBridge {

    private static final String TAG = "JSBridge";
    private static final String BRIDGE_TAG = "DuduluJSBridge";
    private static final String USER_AGENT = "DuduluGame/1.0.0";
    private static JSBridge instance = null;

    private Integer idCounter = 1;                                                                 // identifier for each async request
    private HashMap<Integer, JSBridgeRequest> requests = new HashMap<Integer, JSBridgeRequest>();  // storing requests currently using the bridge
    private ArrayList<JSBridgeRequest> queue = new ArrayList<JSBridgeRequest>();                   // queue for storing request before the html is ready
    private ArrayList<String> scripts = new ArrayList<String>();                                   // script to run before the queue is run
    private boolean isPageReady = false;
    private boolean isScriptReady = false;
    private int loadingScriptCount = 0;

    private boolean loadingFinished = true;
    private boolean redirect = false;

    private BrowserActivity mContext;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private MenuItem menu_item_refresh;

    /**
     * Constructor for JSBridge
     *
     * @param context, application context
     */
    public JSBridge(final BrowserActivity context, String url) {

        mContext = context;
        mWebView = context.getWebView();
        mProgressBar = context.getProgressBar();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setUseWideViewPort(true);

        //DOM cache
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        String dbPath = BootstrapApplication.getInstance().getDir("database", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setDatabasePath(dbPath);
        //App cache
        mWebView.getSettings().setAppCacheEnabled(true);
        String appCaceDir = BootstrapApplication.getInstance().getDir("cache", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setAppCachePath(appCaceDir);

        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 " + USER_AGENT);

//        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //有网络就请求网络,无网络就从缓存中获取网页
        if (NetworkUtils.isNetworkAvailable(BootstrapApplication.getInstance())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        /**
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        /**
         * onExceededDatabaseQuota and setDatabasePath are deprecated in KITKAT,
         * previous versions need to call them to activate Web SQL in mWebView
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String databasePath = context.getDir(BRIDGE_TAG, Context.MODE_PRIVATE).getPath();
            mWebView.getSettings().setDatabasePath(databasePath);
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                    quotaUpdater.updateQuota(estimatedSize * 2);
                }
            });
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                mWebView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                mProgressBar.setVisibility(View.VISIBLE);
                context.setSupportProgressBarIndeterminateVisibility(true);
                menu_item_refresh = context.getMenu_item_refresh();
                if (menu_item_refresh != null) {
                    menu_item_refresh.setVisible(false);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    //HIDE LOADING IT HAS FINISHED
                    mProgressBar.setVisibility(View.GONE);
                    context.setSupportProgressBarIndeterminateVisibility(false);
                    menu_item_refresh = context.getMenu_item_refresh();
                    if (menu_item_refresh != null) {
                        menu_item_refresh.setVisible(true);
                    }
                } else {
                    redirect = false;
                }

                /**
                 * JSBridge
                 */
//                injectScriptFile(view, "js/json2.js");

                Log.v(TAG, "onPageFinished - url: " + url);
                isPageReady = true;
                if (scripts.size() > 0) {
                    loadScript();
                } else {
                    runAsyncJs();
                }

            }

        });

        mWebView.addJavascriptInterface(this, BRIDGE_TAG);
        mWebView.loadUrl(url);

    }

    /**
     * make the bridge becomes singleton
     *
     * @param context, application context
     * @return an instance of JSBridge
     */
    public static JSBridge getInstance(BrowserActivity context, String url) {
        if (instance == null) {
            instance = new JSBridge(context, url);
        }
        return instance;
    }

    /**
     * Load in a js file
     *
     * @param url of the js file
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * Load in a js file
     *
     * @param uri of the js file
     */
    public void loadScript(String uri) {
        scripts.add(uri);
        loadScript();
    }

    /**
     * Load all scripts from the scripts queue before loading the methods in javascript
     */
    private void loadScript() {
        if (isPageReady && scripts.size() > 0) {
//            String src = scripts.remove(0);
//            mWebView.loadUrl("javascript:(function() {var script=document.createElement('script'); script.type='text/javascript'; script.src='" + src + "'; script.onload=function(){window."+BRIDGE_TAG+".scriptOnLoad()}; document.getElementsByTagName('head').item(0).appendChild(script);})()");
//            mWebView.loadDataWithBaseURL("file:///android_asset/", htmlStr, "text/html", "UTF-8", null);
            injectScriptFile(mWebView, scripts.remove(0));
            mWebView.loadUrl("javascript:(function() {window." + BRIDGE_TAG + ".scriptOnLoad()})()");
            loadingScriptCount++;
            loadScript();
        }
    }

    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = BootstrapApplication.getInstance().getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the javascript function without arguments
     *
     * @param func     name of the javascript function
     * @param callback for success callback
     */
    public void runAsyncJs(String func, JSBridgeRequest.BridgeCallback callback) {
        runAsyncJs(func, null, callback);
    }

    /**
     * Run the javascript function with arguments
     * a key is inserted in the parameter to keep track of the javascript return to a specific object instance
     *
     * @param func     function name to call
     * @param params   parameters to pass to the functions
     * @param callback success callback from javascript
     */
    public void runAsyncJs(String func, String params, JSBridgeRequest.BridgeCallback callback) {
        Integer key = idCounter++;
        JSBridgeRequest req = new JSBridgeRequest(key, func, params, callback);
        requests.put(key, req);
        queue.add(req);
        runAsyncJs();
    }

    /**
     * Run code under 2 conditions
     * HTML is already loaded and
     * queue size > 0
     */
    private void runAsyncJs() {
        if (isPageReady && isScriptReady && queue.size() > 0) {
            Log.d(TAG, "runAsyncJs()");
            final JSBridgeRequest req = queue.remove(0);

            if (req.getParams() == null || req.getParams().isEmpty()) {
                req.setParams("''");
            }

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    Log.d(TAG, "run()");
//                    Log.d(TAG, "javascript: " + "javascript:" + req.getFunc() + "(" + req.getKey() + ")");
                    mWebView.loadUrl("javascript:" + req.getFunc() + "(" + req.getKey() + ", " + req.getParams() + ")");
                }
            }.execute();

            runAsyncJs();
        }
    }

    @JavascriptInterface
    public void scriptOnLoad() {
        Log.d(TAG, "scriptOnLoad()");
        if (--loadingScriptCount == 0) {
            isScriptReady = true;
            runAsyncJs();
        }
    }

    @JavascriptInterface
    public void bridgeResponse(String key, String result) {
        Integer intKey = null;
        try {
            intKey = Integer.parseInt(key);
        } catch (Exception e) {
        }
        if (intKey != null && requests.containsKey(intKey)) {
            JSBridgeRequest req = requests.get(intKey);
            (req.getCallback()).onSuccess(result);
            requests.remove(req);
        } else if (key.equals("log")) {
            Log.v(TAG, result);
        } else {
            // Notification
        }
    }
/*
    @JavascriptInterface
    public void parseShareMessage(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
}
