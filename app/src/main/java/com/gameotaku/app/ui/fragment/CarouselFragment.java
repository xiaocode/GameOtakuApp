package com.gameotaku.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gameotaku.app.R;
import com.gameotaku.app.ui.adapter.BootstrapPagerAdapter;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Fragment which houses the View pager.
 */
public class CarouselFragment extends Fragment {

//    @InjectView(R.id.tpi_header)
//    protected TitlePageIndicator indicator;

//    @InjectView(R.id.tabs)
//    protected PagerSlidingTabStrip tabs;

    @InjectView(R.id.vp_pages)
    protected ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Views.inject(this, getView());

        pager.setAdapter(new BootstrapPagerAdapter(getResources(), getChildFragmentManager()));
//        indicator.setViewPager(pager);
        pager.setCurrentItem(0);

//        // Initialize the ViewPager and set an adapter
//        pager.setAdapter(new BootstrapPagerAdapter(getResources(), getChildFragmentManager()));
//        // Bind the tabs to the ViewPager
//        tabs.setViewPager(pager);
//        pager.setCurrentItem(1);

    }
}