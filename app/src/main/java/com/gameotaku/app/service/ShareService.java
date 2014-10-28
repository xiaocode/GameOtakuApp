package com.gameotaku.app.service;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * Created by Vincent on 9/2/14.
 */
public class ShareService {

    final String wx_appId = "wx967daebe835fbeac";
    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private Context mContext;

    public ShareService(Context context) {
        mContext = context;
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
    }

    public void shareToWeixin() {
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        /*
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, wx_appId);
        wxHandler.addToSocialSDK();
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("你的URL链接");
        weixinContent.setShareImage(localImage);
        mController.setShareMedia(weixinContent);
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, wx_appId);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        circleMedia.setShareImage(localImage);
        circleMedia.setTargetUrl("你的URL链接");
        mController.setShareMedia(circleMedia);
        */
    }

}
