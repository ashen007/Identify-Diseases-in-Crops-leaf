package com.example.planter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class FlashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)

        supportActionBar?.hide()

        Handler().postDelayed({

            if ((isLanguageSelected()) && (isOnBoardingCompleted())) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LanguageSelectionActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 2400)
    }

    private fun isLanguageSelected(): Boolean {
        val sharedPref = getSharedPreferences("isLangSelected", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLangSelected", false)
    }

    private fun isOnBoardingCompleted(): Boolean {
        val sharedPref = getSharedPreferences("isOnBoardingCompleted", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isOnBoardingCompleted", false)
    }
}