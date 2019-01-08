package com.unicorn.signboard.app

import android.os.Environment
import java.io.File

class ConfigUtils {

    companion object {
        fun baseDir(): String {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val baseDir = File(downloadDir, "SxMobileOA")
            if (!baseDir.exists()) baseDir.mkdir()
            return baseDir.absolutePath
        }


//        const val baseUrl2 = "http://chue59.natappfree.cc/signboard"

//        const val baseUrl2 = "https://lhsrkj.com/signboard"
        const val baseUrl2 = "http://106.14.125.9:80/signboard"
        const val baseUrl = "$baseUrl2/"
    }

}


