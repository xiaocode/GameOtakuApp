package com.gameotaku.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.gameotaku.app.Injector;
import com.squareup.otto.Bus;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public class BootstrapFragmentActivity extends ActionBarActivity {

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        Views.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
        MobclickAgent.onPause(this);
    }
}
