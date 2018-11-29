package com.unicorn.signboard.login.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.github.florent37.rxsharedpreferences.RxSharedPreferences
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.util.DialogUtils
import com.unicorn.signboard.login.model.LoginParam
import com.unicorn.signboard.main.ui.MainAct
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.act_login.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult,SetTextI18n")
class LoginAct : BaseAct() {

    override val layoutId = R.layout.act_login

    override fun initViews() {
//        etPhoneNo.setText("18501655270")
//        etVerifyCode.setText("123")
    }

    private fun loginIfHasToken() {
        val logout = intent.getBooleanExtra(Key.logout, false)
        if (logout) return
        RxSharedPreferences.with(this).getString(Key.token, "").subscribe { token ->
            if (token.isEmpty()) return@subscribe

            val mask = DialogUtils.showMask(this@LoginAct, "登录中...")
            AppTime.api.loginByToken2(token).observeOnMain(this).subscribeBy(
                onNext = {
                    mask.dismiss()
                    if (!it.success) {
                        ToastUtils.showShort(it.message)
                        return@subscribeBy
                    }
                    AppTime.loginResponse = it
                    RxSharedPreferences.with(this@LoginAct).putString(Key.token, it.loginToken).subscribe()
                    startActivity(Intent(this@LoginAct, MainAct::class.java))
                    finish()
                },
                onError = {
                    mask.dismiss()
                })
        }
    }

    override fun bindIntent() {
        loginIfHasToken()
        btnLoginClicks()
        tvVerifyCodeClicks()
    }

    private fun btnLoginClicks() {
        btnLogin.safeClicks().subscribe {
            val phoneNo = etPhoneNo.trimText()
            if (TextUtils.isEmpty(phoneNo)) {
                ToastUtils.showShort("手机号不能为空")
                return@subscribe
            }
            val verifyCode = etVerifyCode.trimText()
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort("验证码不能为空")
                return@subscribe
            }
            fun login(loginParam: LoginParam) {
                val mask = DialogUtils.showMask(this, "登录中...")
                AppTime.api.login(loginParam).observeOnMain(this).subscribeBy(
                    onNext = {
                        mask.dismiss()
                        if (!it.success) {
                            ToastUtils.showShort(it.message)
                            return@subscribeBy
                        }
                        AppTime.loginResponse = it
                        RxSharedPreferences.with(this@LoginAct).putString(Key.token, it.loginToken).subscribe()
                        startActivity(Intent(this@LoginAct, MainAct::class.java))
                        finish()
                    },
                    onError = {
                        mask.dismiss()
                    }
                )
            }
            login(LoginParam(phoneNo, verifyCode))
        }
    }

    private fun tvVerifyCodeClicks() {
        fun getVerifyCode(phoneNo: String) {
            AppTime.api.getVerifyCode(phoneNo).observeOnMain(this).subscribe {
                if (it.success) ToastUtils.showShort("验证码已发送")
                else ToastUtils.showShort(it.error)
            }
        }
        tvVerifyCode.setOnClickListener { _ ->
            val phoneNo = etPhoneNo.trimText()
            if (TextUtils.isEmpty(phoneNo)) {
                ToastUtils.showShort("手机号不能为空")
                return@setOnClickListener
            }
            val count = 60L
            Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map { count - it }
                .doOnSubscribe {
                    getVerifyCode(phoneNo)
                    tvVerifyCode.setOnClickListener(null)
                    tvVerifyCode.setBackgroundColor(ContextCompat.getColor(this@LoginAct, R.color.md_grey_300))
                }
                .observeOnMain(this)
                .subscribeBy(
                    onNext = { tvVerifyCode.text = "已发送(${it}秒)" },
                    onComplete = {
                        tvVerifyCode.text = "获取验证码"
                        tvVerifyCode.setBackgroundColor(ContextCompat.getColor(this@LoginAct, R.color.md_green_400))
                        tvVerifyCodeClicks()
                    }
                )
        }
    }

}
