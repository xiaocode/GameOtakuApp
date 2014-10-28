package com.gameotaku.app.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gameotaku.app.R;
import com.gameotaku.app.browser.JSBridge;
import com.gameotaku.app.browser.JSBridgeRequest;
import com.gameotaku.app.service.Constants;
import com.gameotaku.app.util.AjustImageUtils;
import com.gameotaku.app.util.NetworkUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import butterknife.Views;

public class BrowserActivity extends ActionBarActivity {
    final String wx_appId = "wxe61c571843f62495";
    final Context mContext = this;
    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    @InjectView(R.id.browser_container)
    protected RelativeLayout mContainer;
    @InjectView(R.id.webView)
    protected WebView mWebView;
    @InjectView(R.id.pb_loading)
    protected ProgressBar mProgressBar;
    /**
     * Refresh Button
     */
    protected MenuItem menu_item_refresh;
    long item_id;
    String mName;
    String mUrl;
    String mIcon;
    private JSBridge jsBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initShareConfig();

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        Intent intent = getIntent();
        item_id = intent.getLongExtra(Constants.Intent.BROWSER_ITEM_ID, 0L);
        mName = intent.getStringExtra(Constants.Intent.BROWSER_ITEM_NAME);
        mUrl = intent.getStringExtra(Constants.Intent.BROWSER_ITEM_URL);
        mIcon = intent.getStringExtra(Constants.Intent.BROWSER_ITEM_ICON);

        setTitle(mName);
        getSupportActionBar().setIcon(AjustImageUtils.getInstance().loadImage(item_id, mIcon, "w_icon"));

        setContentView(R.layout.activity_browser);
        Views.inject(this);

        //umeng push
        PushAgent.getInstance(this).onAppStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // create new ProgressBar and style it
            mProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            mProgressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 24));

            // retrieve the top view of our application
            final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
            decorView.addView(mProgressBar);

            // Here we try to position the ProgressBar to the correct position by looking
            // at the position where content area starts. But during creating time, sizes
            // of the components are not set yet, so we have to wait until the components
            // has been laid out
            // Also note that doing progressBar.setY(136) will not work, because of different
            // screen densities and different sizes of actionBar
            ViewTreeObserver observer = mProgressBar.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    View contentView = decorView.findViewById(android.R.id.content);

                    mProgressBar.setY(contentView.getY() - 8);

                    ViewTreeObserver observer = mProgressBar.getViewTreeObserver();
                    observer.removeGlobalOnLayoutListener(this);
                }
            });
        } else {
            mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        }

//        JSBridge.getInstance(this).loadUrl("http://duduluwan.duapp.com/games/shuqian4/index.html");
//        JSBridge.getInstance(this).loadScript("./json2.js");
//        mUrl = "http://duduluwan.duapp.com/games/shuqian4/index.html";
        jsBridge = new JSBridge(this, mUrl);
        jsBridge.loadScript("js/demo.js");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mContainer.removeAllViews();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser, menu);
//        menu.findItem(R.id.action_share).setVisible(false);
        menu_item_refresh = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_share:
                openShareMenu();
                return true;
            case R.id.action_refresh:
                if (!NetworkUtils.isNetworkAvailable(mContext)) {
                    Toast.makeText(this, "网络连接不可用,无法更新数据", Toast.LENGTH_LONG).show();
                    return true;
                }
                mWebView.clearCache(true);
                mWebView.loadUrl(mUrl);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.isFocused() && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
            mContainer.removeAllViews();
            mWebView.removeAllViews();
            mWebView.destroy();
            finish();
        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public MenuItem getMenu_item_refresh() {
        return menu_item_refresh;
    }

    private void openShareMenu() {
        jsBridge.runAsyncJs("sendShareMessage", new JSBridgeRequest.BridgeCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.v("JSONString", "JSONString:" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("JSONString", "JSONString link:" + jsonObject.getString("link"));

                    //发送给朋友
                    WeiXinShareContent weixinContent = new WeiXinShareContent();
                    weixinContent.setShareContent(jsonObject.getString("desc"));
                    weixinContent.setTitle(jsonObject.getString("title"));
                    weixinContent.setTargetUrl(jsonObject.getString("link"));
                    weixinContent.setShareImage(new UMImage(mContext, jsonObject.getString("img_url")));
                    mController.setShareMedia(weixinContent);

                    //发送到朋友圈
                    CircleShareContent circleMedia = new CircleShareContent();
                    circleMedia.setShareContent(jsonObject.getString("desc"));
                    circleMedia.setTitle(jsonObject.getString("title"));
                    circleMedia.setShareImage(new UMImage(mContext, jsonObject.getString("img_url")));
                    circleMedia.setTargetUrl(jsonObject.getString("link"));
                    mController.setShareMedia(circleMedia);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mController.openShare(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void initShareConfig() {
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, wx_appId);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, wx_appId);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


        //设置新浪SSO handler
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());


    }

}
