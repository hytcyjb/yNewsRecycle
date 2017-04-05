package yjbo.yy.ynewsrecycle;

import android.app.Application;

import com.orhanobut.logger.Logger;

import yjbo.yy.ynewsrecycle.mainutil.LogUtils;

/**
 * 主application
 *
 * @author yjbo
 * @time 2017/4/3 12:08
 */
public class mainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG){
            new LogUtils(true);
        }
    }
}
