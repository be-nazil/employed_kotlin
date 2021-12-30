package com.nb.employed.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nb.employed.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startAnim()
        Handler().postDelayed({
            redirect()
            finish()
        }, 3000)
    }

    private fun startAnim() {
        val slideAnimationL = AnimationUtils.loadAnimation(this, R.anim.slide_from_right_to_left)
        text_view_header.startAnimation(slideAnimationL)
        text_view_min.startAnimation(slideAnimationL)
    }

    private fun redirect() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}