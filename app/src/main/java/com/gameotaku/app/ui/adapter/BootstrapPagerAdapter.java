package com.gameotaku.app.ui.adapter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gameotaku.app.R;
import com.gameotaku.app.ui.fragment.WebGameFragment;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
//                result = new UserCenterFragment();
                result = new WebGameFragment();
                break;
//            case 1:
//                result = new WebGameFragment();
//                break;
//            case 2:
//                result = new WebGameFragment();
//                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_web_game);
//                return resources.getString(R.string.page_user_center);
//            case 1:
//                return resources.getString(R.string.page_web_game);
//            case 2:
//                return resources.getString(R.string.page_checkins);
            default:
                return null;
        }
    }
}
