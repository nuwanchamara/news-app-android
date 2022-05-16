package com.test.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.test.newsapp.ui.activities.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        startActivity()
    }


    override fun onResume() {
        super.onResume()

    }

    fun startActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}