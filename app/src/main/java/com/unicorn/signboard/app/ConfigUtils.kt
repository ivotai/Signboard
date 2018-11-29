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

        const val baseUrl = "https://lhsrkj.com/signboard/"
        const val baseUrl2 = "https://lhsrkj.com/signboard"
//        const val baseUrl = "http://y9n3f9.natappfree.cc/signboard/"
    }

}


