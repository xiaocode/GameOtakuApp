package com.gameotaku.app;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import com.gameotaku.app.service.Constants;
import com.gameotaku.app.ui.activity.MainActivity;
import com.gameotaku.app.util.ConfigUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Android Bootstrap application
 */
public class BootstrapApplication extends Application {

    private static BootstrapApplication instance;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Perform injection
        Injector.init(getRootModule(), this);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                ConfigUtils.setBoolean(BootstrapApplication.getInstance(), Constants.SharedPreference.FORCE_UPDATE, true);
                super.launchApp(context, uMessage);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                ConfigUtils.setBoolean(BootstrapApplication.getInstance(), Constants.SharedPreference.FORCE_UPDATE, true);
                Intent intent = new Intent(getInstance(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }

    private Object getRootModule() {
        return new RootModule();
    }
}
