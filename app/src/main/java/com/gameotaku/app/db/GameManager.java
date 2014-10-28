package com.gameotaku.app.db;

import android.util.Log;

import com.gameotaku.app.db.dao.DownloadGame;
import com.gameotaku.app.db.dao.DownloadGameDao;
import com.gameotaku.app.db.dao.MyGame;
import com.gameotaku.app.db.dao.MyGameDao;
import com.gameotaku.app.db.dao.NativeGame;
import com.gameotaku.app.db.dao.NativeGameDao;
import com.gameotaku.app.db.dao.WebGame;
import com.gameotaku.app.db.dao.WebGameDao;
import com.gameotaku.app.service.Constants;

import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by Vincent on 6/21/14.
 */
public class GameManager {

    private static GameManager instance;
    private DbHelper mDbHelper;
    private MyGameDao myGameDao;
    private NativeGameDao nativeGameDao;
    private WebGameDao webGameDao;
    private DownloadGameDao downloadGameDao;

    public GameManager() {
        mDbHelper = DbHelper.getInstance();
        myGameDao = mDbHelper.getDaoSession().getMyGameDao();
        nativeGameDao = mDbHelper.getDaoSession().getNativeGameDao();
        webGameDao = mDbHelper.getDaoSession().getWebGameDao();
        downloadGameDao = mDbHelper.getDaoSession().getDownloadGameDao();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public NativeGameDao getGameDao() {
        return nativeGameDao;
    }

    public synchronized List<NativeGame> getGameList() {
        List<NativeGame> games = nativeGameDao.queryBuilder().list();
        if (games.size() > 0) {
            return games;
        } else {
            return null;
        }
    }

    public synchronized List<MyGame> getMyGameList() {
        List<MyGame> myGames = myGameDao.queryBuilder().where(MyGameDao.Properties.Status.notEq(DownloadConstants.STATUS_UNINSTALLED)).list();
        if (myGames.size() > 0) {
            return myGames;
        } else {
            return null;
        }
    }

    public synchronized List<WebGame> getWebGameList(Map<String, String> options) {
        List<WebGame> webGames;
        if (options != null) {
            if (options.containsKey("type") && !options.get("type").equals("")) {
                WhereCondition condition_type = WebGameDao.Properties.Type.eq(options.get("type"));
                webGames = webGameDao.queryBuilder().where(condition_type).list();
            } else {
                webGames = webGameDao.queryBuilder().list();
            }
        } else {
            webGames = webGameDao.queryBuilder().list();
        }
        if (webGames.size() > 0) {
            return webGames;
        } else {
            return null;
        }
    }

    public synchronized MyGame getMyGame(long item_id) {
        List myGames = myGameDao.queryBuilder().where(MyGameDao.Properties.Item_id.eq(item_id)).list();
        if (myGames.size() > 0) {
            return (MyGame) myGames.get(0);
        } else {
            return null;
        }
    }

    public synchronized MyGame getMyGame(NativeGame game) {
        List myGames = myGameDao.queryBuilder().where(MyGameDao.Properties.Item_id.eq(game.getItem_id())).list();
        if (myGames.size() > 0) {//如果已存在下载
            return (MyGame) myGames.get(0);
        } else {
            return null;
        }
    }

    public synchronized NativeGame getGame(long item_id) {
        List games = nativeGameDao.queryBuilder().where(NativeGameDao.Properties.Item_id.eq(item_id)).list();
        if (games.size() > 0) {//如果已存在下载
            return (NativeGame) games.get(0);
        } else {
            return null;
        }
    }

    public synchronized NativeGame getGame(NativeGame game) {
        List games = nativeGameDao.queryBuilder().where(NativeGameDao.Properties.Item_id.eq(game.getItem_id())).list();
        if (games.size() > 0) {//如果已存在下载
            return (NativeGame) games.get(0);
        } else {
            return null;
        }
    }

    public synchronized WebGame getWebGame(long item_id) {
        List webgames = webGameDao.queryBuilder().where(WebGameDao.Properties.Item_id.eq(item_id)).list();
        if (webgames.size() > 0) {
            return (WebGame) webgames.get(0);
        } else {
            return null;
        }
    }

    public synchronized long addGame(NativeGame game) {
        NativeGame game1 = getGame(game.getItem_id());
        if (game1 == null) {
//            long id = nativeGameDao.insert(game);
//            Log.v("MyGameManager","insert new game , id : "+id);
//            if (id > 0) {
//                Log.v("MyGameManager","insert new game , name : "+game.getName());
//            }
            return nativeGameDao.insert(game);
        } else {
            game.setLocal_id(game1.getLocal_id());
            nativeGameDao.update(game);
//            Log.v("MyGameManager","update exist game , name : "+game.getName());
            return game.getLocal_id();
        }
    }

    public synchronized MyGame addMyGame(NativeGame game) {
//        MyGame myGame = getMyGame(game);
//        if (myGame != null) return myGame;

        //addGame里会自动判断是否已经添加
//        addGame(game);

        MyGame myGame = new MyGame();
        myGame.setItem_id(game.getItem_id());
        myGame.setPackage_name(game.getPackage_name());
        myGame.setStatus(DownloadConstants.STATUS_WAIT);
        myGame.setVersion_code(game.getVersion_code());
        myGame.setVersion_name(game.getVersion_name());
        long myGameId = myGameDao.insert(myGame);

        Log.d("MyGameManager", "add myGame ! game name is :" + game.getName() + " myGameId : " + myGameId);

//        MyGame myGame1 = getMyGame(myGame.getItem_id());
//        if (myGame1!=null) {
//            Log.d("MyGameManager", "find new myGame,Item_id is "+myGame1.getItem_id()+" ,status is "+myGame1.getStatus());
//        }
//
//        List myGames = myGameDao.queryBuilder().where(MyGameDao.Properties.Item_id.eq(myGame.getItem_id())).list();
//        List myGames1 = myGameDao.myGameDao.queryBuilder()().where(MyGameDao.Properties.Item_id.eq(myGame.getItem_id())).list();

        return myGame;
    }

    public synchronized long addNewMyGame(MyGame myGame) {
        return myGameDao.insert(myGame);
    }

    public synchronized long addWebGame(WebGame webGame) {
        Log.v(Constants.LogTag.GAMEMANAGER_TAG, "add new WebGame, name : " + webGame.getName());
        return webGameDao.insert(webGame);
    }

    public synchronized void updateWebGame(WebGame webGame) {
        Log.v(Constants.LogTag.GAMEMANAGER_TAG, "update new WebGame, name : " + webGame.getName());
        webGameDao.update(webGame);
    }

    public synchronized void removeMyGame(long item_id) {
        MyGame myGame = getMyGame(item_id);
        if (myGame != null) {
            myGameDao.delete(myGame);
        }
    }

    public synchronized void updateMyGame(NativeGame game) {
        MyGame myGame = getMyGame(game);
        if (myGame != null) {
            myGame.setVersion_code(game.getVersion_code());
            myGame.setVersion_name(game.getVersion_name());
            myGameDao.update(myGame);
            Log.d("MyGameManager", "update myGame !");
        } else {
            addMyGame(game);
        }
    }

    public synchronized void updateMyGame(MyGame myGame) {
        myGameDao.update(myGame);
    }

    public synchronized void pauseMyGame(long Item_id) {
        MyGame myGame = getMyGame(Item_id);
        Log.d("MyGameManager", "update myGame status paused! but do not know if the myGame exist!");
        if (myGame != null) {
            myGame.setStatus(DownloadConstants.STATUS_PAUSE);
            myGameDao.update(myGame);
            Log.d("MyGameManager", "update myGame status paused!");
        }
    }

    public synchronized void pauseMyGame(NativeGame game) {
        MyGame myGame = getMyGame(game);
        if (myGame != null) {
            myGame.setStatus(DownloadConstants.STATUS_PAUSE);
            myGameDao.update(myGame);
        }
    }

    public synchronized void resumeMyGame(NativeGame game) {
        MyGame myGame = getMyGame(game);
        if (myGame != null) {
            myGame.setStatus(DownloadConstants.STATUS_WAIT);
            myGameDao.update(myGame);
            Log.d("MyGameManager", "update myGame status resume!");
        }
    }

    public synchronized void completeMyGame(long Item_id, String path) {
        NativeGame game = getGame(Item_id);
        if (game != null) {
            completeMyGame(game, path);
        }
    }

    public synchronized void completeMyGame(NativeGame game, String path) {
        MyGame myGame = getMyGame(game);
        if (myGame != null) {
            myGame.setStatus(DownloadConstants.STATUS_COMPLETED);
            myGameDao.update(myGame);
            Log.d("MyGameManager", "update myGame status complete!");
        }
    }

    public synchronized void installMyGame(NativeGame game) {
        MyGame myGame = getMyGame(game);
        if (myGame != null) {
            myGame.setStatus(DownloadConstants.STATUS_INSTALLED);
            myGameDao.update(myGame);
        }
    }

    public synchronized void deleteMyGame(long item_id) {
        MyGame myGame = getMyGame(item_id);
        if (myGame != null) {
            myGameDao.delete(myGame);
//            NativeGame game1 = getGame(game);
//            nativeGameDao.delete(game1);
        }
    }

    public synchronized List<DownloadGame> getDownloadGameList() {
        return downloadGameDao.loadAll();
    }

    public synchronized void addDownloadGame(NativeGame game, String fileName, String tempFileName) {
        DownloadGame dg = new DownloadGame();
        dg.setItem_id(game.getItem_id());
        dg.setPackage_name(game.getPackage_name());
        dg.setRequest_url(game.getRequest_url());
        dg.setProgress(0);
        dg.setTotal_size(0L);
        dg.setTemp_name(tempFileName);
        dg.setApk_name(fileName);
        dg.setStatus(DownloadConstants.STATUS_WAIT);
        downloadGameDao.insert(dg);
    }

    public synchronized void setDownloadGameStatus(long item_id, int status) {
        DownloadGame dg = getDownloadGame(item_id);
        if (dg != null) {
            dg.setStatus(status);
            downloadGameDao.update(dg);
        }
    }

    public synchronized void setDownloadGameTotalSize(long item_id, long size) {
        DownloadGame dg = getDownloadGame(item_id);
        if (dg != null) {
            dg.setTotal_size(size);
            downloadGameDao.update(dg);
        }
    }

    public synchronized void setDownloadGameCurrentSize(long item_id, long size) {
        DownloadGame dg = getDownloadGame(item_id);
        if (dg != null) {
            dg.setTotal_size(size);
            downloadGameDao.update(dg);
        }
    }

    public synchronized DownloadGame getDownloadGame(long item_id) {
        List list = downloadGameDao.queryBuilder().where(DownloadGameDao.Properties.Item_id.eq(item_id)).list();
        if (list.size() > 0) {
            return (DownloadGame) list.get(0);
        } else {
            return null;
        }
    }

    public synchronized void deleteDownloadGame(long item_id) {
        DownloadGame dg = getDownloadGame(item_id);
        if (dg != null) {
            downloadGameDao.delete(dg);
        }
    }

}
