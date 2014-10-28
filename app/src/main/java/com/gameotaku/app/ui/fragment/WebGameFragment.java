package com.gameotaku.app.ui.fragment;

import android.accounts.OperationCanceledException;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.gameotaku.app.BootstrapApplication;
import com.gameotaku.app.BootstrapServiceProvider;
import com.gameotaku.app.Injector;
import com.gameotaku.app.R;
import com.gameotaku.app.authenticator.LogoutService;
import com.gameotaku.app.db.GameManager;
import com.gameotaku.app.db.GameType;
import com.gameotaku.app.db.dao.WebGame;
import com.gameotaku.app.service.Constants;
import com.gameotaku.app.ui.ThrowableLoader;
import com.gameotaku.app.ui.adapter.GameTypeAdapter;
import com.gameotaku.app.ui.adapter.WebGameAdapter;
import com.gameotaku.app.util.ConfigUtils;
import com.gameotaku.app.util.NetworkUtils;
import com.gameotaku.app.util.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class WebGameFragment extends GameListFragment<WebGame> implements GameTypeAdapter.TypeChooseListener {
    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    protected LogoutService logoutService;

    private Button mHotButton;
    private Button mCategoryButton;
    private PopupWindow popupWindow;

    private ArrayList<GameType> gameTypeArrayList;
    private GameTypeAdapter gameTypeAdapter;
    private ListView gameTypeListView;

    private String current_type = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        gameTypeArrayList = new ArrayList<GameType>();

        gameTypeArrayList.add(new GameType("动作游戏", "动作游戏"));
        gameTypeArrayList.add(new GameType("益智游戏", "益智游戏"));
        gameTypeArrayList.add(new GameType("休闲游戏", "休闲游戏"));
        gameTypeArrayList.add(new GameType("", "所有游戏"));

        gameTypeAdapter = new GameTypeAdapter(getActivity(), R.layout.game_type_item, this);
        gameTypeAdapter.updateData(gameTypeArrayList);
        gameTypeListView = new ListView(getActivity());
        gameTypeListView.setAdapter(gameTypeAdapter);
        /*
        gameTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_type = gameTypeArrayList.get(i).getType_tag();
                Log.v("dudulu_debug", "current_type : " + current_type);
                forceRefresh();
                if (popupWindow!=null) {
                    popupWindow.dismiss();
                }
            }
        });
        */
    }

    @Override
    public void setTypeChooser(GameType gameType) {
        current_type = gameType.getType_tag();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(gameType.getType_name());
            }
        }
        Log.v("dudulu_debug", "current_type : " + current_type);
        forceRefresh();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_game, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Animation slide_up = AnimationUtils.loadAnimation(BootstrapApplication.getInstance(), R.anim.menu_slide_up);
        final Animation slide_down = AnimationUtils.loadAnimation(BootstrapApplication.getInstance(), R.anim.menu_slide_down);

        mHotButton = (Button) view.findViewById(R.id.action_hot);
        mCategoryButton = (Button) view.findViewById(R.id.action_category);

        mHotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_type = "";
                Log.v("dudulu_debug", "current_type : " + current_type);
                forceRefresh();
            }
        });

        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mSlideMenu.getTag() == null || mSlideMenu.getTag().equals("close")) {
////                    mSlideMenu.startAnimation(slide_up);
//                    slideToTop(mSlideMenu);
//                    mSlideMenu.setTag("open");
//                } else {
////                    mSlideMenu.startAnimation(slide_down);
//                    slideToBottom(mSlideMenu);
//                    mSlideMenu.setTag("close");
//                }
                showPopupWindow(0, 0);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WebGameFragment");
        //是否强制更新数据,根据notification的通知来!
        boolean force_update = ConfigUtils.getBoolean(BootstrapApplication.getInstance(), Constants.SharedPreference.FORCE_UPDATE);
        if (force_update) {
            ConfigUtils.setLong(BootstrapApplication.getInstance(), Constants.SharedPreference.UPDATE_TIME, 0);
            current_type = "";
            forceRefresh();
            ConfigUtils.setBoolean(BootstrapApplication.getInstance(), Constants.SharedPreference.FORCE_UPDATE, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("WebGameFragment");
    }

    private void showPopupWindow(int xoff, int yoff) {
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(UIUtils.getScreenWidth(getActivity()) / 2);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 100)));
//        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(gameTypeListView);
        popupWindow.showAsDropDown(mCategoryButton, xoff, yoff);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_games);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
//        listView.setDividerHeight(0);
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);
        super.onDestroyView();
    }

    @Override
    public Loader<List<WebGame>> onCreateLoader(int id, Bundle args) {
        final List<WebGame> initialItems = items;
        return new ThrowableLoader<List<WebGame>>(getActivity(), items) {

            @Override
            public List<WebGame> loadData() throws Exception {
                //如果网络可用,从网络读取 and 更新间隔大于60分钟
                long last_update_time = ConfigUtils.getLong(BootstrapApplication.getInstance(), Constants.SharedPreference.UPDATE_TIME);
                long current_time = System.nanoTime();
                boolean need_update = ((current_time - last_update_time) - (Math.pow(10, 9) * 60 * 10)) > 0;
                //设置筛选条件
                Map<String, String> options = new HashMap<String, String>() {
                };
                if (!current_type.equals("")) options.put("type", current_type);
                //执行判断
                if (need_update && NetworkUtils.isNetworkAvailable(getContext())) {
                    Log.v(Constants.LogTag.LOADER_TAG, "retrieve WebGames from Internet!");
                    try {
                        if (getActivity() != null) {
                            List<WebGame> list = serviceProvider.getService(getActivity()).getWebGames(options);
                            //保存到数据库,存在则进行更新
                            if (list != null && list.size() > 0) {
                                for (WebGame wg : list) {
                                    WebGame webGame = GameManager.getInstance().getWebGame(wg.getItem_id());
                                    if (webGame == null) {
                                        GameManager.getInstance().addWebGame(wg);
                                    } else {
                                        wg.setLocal_id(webGame.getLocal_id());//反过来赋予新数据对象主键,进行更新
                                        GameManager.getInstance().updateWebGame(wg);
                                    }
                                }
                            } else {
                                //列表为空返回空数组
                                list = Collections.emptyList();
                            }
                            ConfigUtils.setLong(BootstrapApplication.getInstance(), Constants.SharedPreference.UPDATE_TIME, System.nanoTime());
                            return list;
                        } else {
                            return Collections.emptyList();
                        }
                    } catch (OperationCanceledException e) {
                        Activity activity = getActivity();
                        if (activity != null)
                            activity.finish();
                        return initialItems;
                    }
                } else {//如果网络不可用，从数据库读取 or 更新时间不到60分钟
                    Log.v(Constants.LogTag.LOADER_TAG, "retrieve WebGames from Local Database!");
                    List<WebGame> list = GameManager.getInstance().getWebGameList(options);
                    if (list != null && list.size() > 0) {
                        return list;
                    } else {
                        return Collections.emptyList();
                    }
                }
            }
        };
    }

    @Override
    protected WebGameAdapter createAdapter() {
        return new WebGameAdapter(getActivity(), R.layout.webgame_list_item);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }
}
