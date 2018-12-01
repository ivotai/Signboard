package com.unicorn.signboard.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.reactivex.plugins.RxJavaPlugins
import net.danlew.android.joda.JodaTimeAndroid

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        fun init(application: Application) {
            Utils.init(application)
            Logger.addLogAdapter(AndroidLogAdapter())
            Stetho.initializeWithDefaults(application)
            JodaTimeAndroid.init(application)
            AppTime.init()
            RxJavaPlugins.setErrorHandler {
                Logger.e(it.toString())
            }
        }
        init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}