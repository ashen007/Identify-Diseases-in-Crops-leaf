package com.example.planter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planter.databinding.ActivityCategoryBinding
import com.example.planter.fragments.FragmentCrops
import com.example.planter.fragments.FragmentCultTipResults
import com.example.planter.fragments.FragmentLevel

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var LANGUAGE: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        LANGUAGE = sharedPref.getString("language", "english").toString()

        val actionBar = supportActionBar

        if (LANGUAGE == "english") {
            if (actionBar != null) {
                actionBar.title = "Cultivation Tips"
            }
        } else if (LANGUAGE == "sinhala") {
            if (actionBar != null) {
                actionBar.title = "වගා ඉඟි"
            }

        } else if (LANGUAGE == "tamil") {
            if (actionBar != null) {
                actionBar.title = "சாகுபடி குறிப்புகள்"
            }

        }

        actionBar?.setDisplayHomeAsUpEnabled(false)

    }

}