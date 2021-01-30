package com.example.orangeline

import java.util.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var keyHash = Utility.getKeyHash(this)
        Log.i("keyhash: ", keyHash) // oTvsY0O2qpncZ/7FncqHRR0Jl5Q=

    }
}