package com.psycho;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by mr.psycho on 2017/1/11.
 */

public class DemoApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //只有调试模式下 才启用日志输出
//        if(BuildConfig.DEBUG){
//            Logger.init("DemoTAG").hideThreadInfo().setMethodCount(0);
//        }else{
//            Logger.init("DemoTAG").setLogLevel(LogLevel.NONE);
//        }

        Logger.init("测试TAG").isShowThreadInfo();
    }
}
