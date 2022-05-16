package com.test.newsapp.ui.activities.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.newsapp.R

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
    }
}