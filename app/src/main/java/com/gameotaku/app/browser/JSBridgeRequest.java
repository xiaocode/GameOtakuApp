package com.gameotaku.app.browser;

import java.util.Date;

/**
 * Created by Vincent on 9/6/14.
 */
public class JSBridgeRequest {

    private Integer key;
    private String func;
    private String params;

    private BridgeCallback callback;

    private Date requestTime;

    /**
     * Contructor for JSBridgeRequest
     *
     * @param key      identifier for each async request
     * @param func     javascript
     * @param params   parameters
     * @param callback callback with success and error handler
     */
    public JSBridgeRequest(Integer key, String func, String params, BridgeCallback callback) {
        this.key = key;
        this.func = func;
        this.params = params;
        this.callback = callback;
        this.requestTime = new Date();
    }

    /**
     * Getter and Setter
     */
    public Integer getKey() {
        return key;
    }

    public String getFunc() {
        return func;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public BridgeCallback getCallback() {
        return callback;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public interface BridgeCallback {
        void onSuccess(String result);
    }

}
