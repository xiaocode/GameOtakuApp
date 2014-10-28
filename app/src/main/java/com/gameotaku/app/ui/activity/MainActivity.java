package com.gameotaku.app.ui.activity;

import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;

import com.gameotaku.app.BootstrapServiceProvider;
import com.gameotaku.app.R;
import com.gameotaku.app.events.NavItemSelectedEvent;
import com.gameotaku.app.service.BootstrapService;
import com.gameotaku.app.ui.fragment.CarouselFragment;
import com.gameotaku.app.ui.fragment.NavigationDrawerFragment;
import com.gameotaku.app.util.Ln;
import com.gameotaku.app.util.SafeAsyncTask;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

import javax.inject.Inject;

import butterknife.Views;


/**
 * Initial activity for the application.
 * <p/>
 * If you need to remove the authentication from the application please see
 * {@link com.gameotaku.app.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    @Inject
    protected BootstrapServiceProvider serviceProvider;
    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    //    private ActionBarDrawerToggle drawerToggle;
//    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        //umeng update
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.silentUpdate(this);

        //umeng feedback
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();

        //umeng statistics
        MobclickAgent.updateOnlineConfig(this);

        //umeng push
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
//        String device_token = UmengRegistrar.getRegistrationId(this);
//        Log.v("umeng_token", "umeng_token : " + device_token);
        mPushAgent.onAppStart();

        if (isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        Views.inject(this);

        // Set up navigation drawer
//        title = drawerTitle = getTitle();
        title = getTitle();

//        if(!isTablet()) {
//            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawerToggle = new ActionBarDrawerToggle(
//                    this,                    /* Host activity */
//                    drawerLayout,           /* DrawerLayout object */
//                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
//                    R.string.navigation_drawer_open,    /* "open drawer" description */
//                    R.string.navigation_drawer_close) { /* "close drawer" description */
//
//                /** Called when a drawer has settled in a completely closed state. */
//                public void onDrawerClosed(View view) {
//                    getSupportActionBar().setTitle(title);
//                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//
//                /** Called when a drawer has settled in a completely open state. */
//                public void onDrawerOpened(View drawerView) {
//                    getSupportActionBar().setTitle(drawerTitle);
//                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//                }
//            };
//
//            // Set the drawer toggle as the DrawerListener
//            drawerLayout.setDrawerListener(drawerToggle);
//
//            navigationDrawerFragment = (NavigationDrawerFragment)
//                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//
//            // Set up the drawer.
//            navigationDrawerFragment.setUp(
//                    R.id.navigation_drawer,
//                    (DrawerLayout) findViewById(R.id.drawer_layout));
//        }


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


//        checkAuth();
        initScreen();

    }

    private boolean isTablet() {
//        return UIUtils.isTablet(this);
        return false;
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
//            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isTablet()) {
//            drawerToggle.onConfigurationChanged(newConfig);
        }
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

    private void initScreen() {
//        if (userHasAuthenticated) {

        Ln.d("Foo");
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CarouselFragment())
                .commit();
//        }

    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

//        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
//            case R.id.timer:
//                navigateToTimer();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch (event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Timer
                navigateToTimer();
                break;
        }
    }
}
