package io.buszi.boomerang

import android.app.Application
import io.buszi.boomerang.core.AndroidBoomerangLogger
import io.buszi.boomerang.core.BoomerangConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        BoomerangConfig.logger = AndroidBoomerangLogger()
    }
}
