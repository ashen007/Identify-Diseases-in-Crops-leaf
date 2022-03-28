package com.example.planter

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.planter.databinding.ActivityOnBoardingHandlingBinding
import com.example.planter.fragments.onboard.*

class OnBoardingHandlingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingHandlingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingHandlingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}