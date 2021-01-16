package com.example.orangeline

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

// Initialize to use Android SDK
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // KakaoSdk initiation
        KakaoSdk.init(this, "888f37712e36c6d3c4d015c855651add")
    }
}
