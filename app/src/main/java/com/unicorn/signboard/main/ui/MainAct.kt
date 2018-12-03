package com.unicorn.signboard.main.ui

import android.annotation.SuppressLint
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.AppUtils
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.app.util.DialogUtils
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.title_recycler.*
import okhttp3.Call
import java.io.File

@SuppressLint("CheckResult")
class MainAct : BaseAct() {

    private val mAdapter = MainAdapter()

    override fun initViews() {
        titleBar.setTitle(title = "主界面", hideBack = true)
        recyclerView.default(mAdapter)
    }

    override fun bindIntent() {
        setAdapterData()
        fetchUsefulData()
        checkUpdate()
    }

    private fun setAdapterData() {
        val version = "版本号: ${AppUtils.getAppVersionName()}"
        listOf(version, "商户录入", "商户列表", "商户统计", "退出").let { mAdapter.setNewData(it) }
    }

    @SuppressLint("CheckResult")
    private fun fetchUsefulData() {
        val api = AppTime.api
        val mask = DialogUtils.showMask(this, "获取数据中...")
        AppTime.api.getDict()
            .flatMap {
                AppTime.dict = it
                return@flatMap api.getArea()
            }.flatMap {
                AppTime.areaList = it
                return@flatMap api.getOperateType()
            }.flatMap {
                AppTime.operateTypeList = it
                return@flatMap api.getHotOperateType()
            }.observeOnMain(this)
            .subscribeBy(
                onNext = {
                    mask.dismiss()
                    AppTime.hotOperateTypeList = it
                },
                onError = {
                    mask.dismiss()
                }
            )
    }

    private fun checkUpdate() {
        fun downloadApk(apkUrl: String) {
            val mask = DialogUtils.showMask2(this, "下载中...")
            OkHttpUtils
                .get()
                .url(apkUrl)
                .build()
                .execute(object : FileCallBack(ConfigUtils.baseDir(), "Signboard.apk") {
                    override fun onResponse(response: File, id: Int) {
                        mask.dismiss()
                        AppUtils.installApp(response)
                    }

                    override fun inProgress(progress: Float, total: Long, id: Int) {
                        mask.setProgress((progress * 100).toInt())
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        mask.dismiss()
                    }
                })
        }

        fun showUpdateDialog(apkUrl: String) {
            MaterialDialog.Builder(this)
                .title("版本更新")
                .cancelable(false)
                .positiveText("确认")
                .onPositive { _, _ -> downloadApk(apkUrl) }
                .show()
        }

        val versionName = AppUtils.getAppVersionName()
        AppTime.api.checkUpdate("1001", versionName).observeOnMain(this).subscribeBy(
            onNext = {
                if (it.newVersion) showUpdateDialog(it.apkUrl)
            },
            onError = {

            }
        )
    }

    override val layoutId = R.layout.title_recycler

}
