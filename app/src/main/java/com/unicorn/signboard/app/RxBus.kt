package com.unicorn.signboard.app

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import florent37.github.com.rxlifecycle.RxLifecycle.disposeOnDestroy
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@SuppressLint("CheckResult")
object RxBus {

    private val subject: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    fun post(obj: Any) {
        subject.onNext(obj)
    }

    fun <T> registerEvent(lifecycleOwner: LifecycleOwner, clazz: Class<T>, consumer: Consumer<T>) {
        subject.
                compose(disposeOnDestroy(lifecycleOwner))
                .ofType(clazz)
                .subscribe(consumer)
    }

}