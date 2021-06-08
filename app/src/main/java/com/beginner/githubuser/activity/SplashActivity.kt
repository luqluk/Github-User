package com.beginner.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.beginner.githubuser.R
import com.beginner.githubuser.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    companion object {
        const val interval : Long = 3000
    }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val slideAnimations = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.splashscreen.startAnimation(slideAnimations)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, interval)
    }
}