package com.unicorn.signboard.app.di

import com.unicorn.signboard.app.api.UniqueApi
import com.unicorn.signboard.app.net.NetModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface AppComponent {

    fun getApi(): UniqueApi

}