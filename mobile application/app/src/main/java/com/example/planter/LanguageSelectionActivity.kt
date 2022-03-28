package com.example.planter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_language_selection.*

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        supportActionBar?.hide()

        val sharedPref = getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        selcet_eng.setOnClickListener {
            val selectedLanguage = "english"

            editor.apply {
                putString("language", selectedLanguage)
                apply()
            }

            languageSelected()

            val intent = Intent(this, OnBoardingHandlingActivity::class.java)
            startActivity(intent)
            finish()
        }

        select_sin.setOnClickListener {
            val selectedLanguage = "sinhala"

            editor.apply {
                putString("language", selectedLanguage)
                apply()
            }

            languageSelected()

            val intent = Intent(this, OnBoardingHandlingActivity::class.java)
            startActivity(intent)
            finish()
        }

        select_tam.setOnClickListener {
            val selectedLanguage = "tamil"

            editor.apply {
                putString("language", selectedLanguage)
                apply()
            }

            languageSelected()

            val intent = Intent(this, OnBoardingHandlingActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun languageSelected() {
        val sharedPref = getSharedPreferences("isLangSelected", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            editor.putBoolean("isLangSelected", true)
            apply()
        }
    }
}