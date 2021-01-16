package com.example.orangeline

import android.util.Log
import android.content.Intent
import android.media.MediaSession2
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken

class LoginActivity : AppCompatActivity() {

    private lateinit var callback: MediaSession2.SessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 공통 callback 함수( 다른 함수의 인자로써 이용되는 함수, 어떤 이벤트에 의해 호출되는 함수)
        val callback: ((OAuthToken?, Throwable?) -> Unit) = { token, error ->
            if (error != null) { // Login Fail
                Log.e(TAG, "로그인 실패", error)
            } else if (token != null) { // Login Success
                Log.i(TAG, "로그인 성공 $(token.accessToken}")
                startMainActivity()
            }
        }

        // kakao login button
        val kakao_login_btn  = findViewById<ImageButton>(R.id.kakao_login_btn)
        kakao_login_btn.setOnClickListener{
            LoginClient.instance.run {
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
               if(isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    loginWithKakaoAccount(this@LoginActivity, callback = callback)
                }
            }
        }
    }
    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
