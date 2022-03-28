package com.example.planter.fragments.onboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board_one.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentOne.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentOne : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_on_board_one, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_one_title.setText("ඔබේ බෝග පරීක්ෂා කරන්න")
            view.on_board_one_dtl.setText("ඒවා සෞඛ්\u200Dය සම්පන්නද නැද්ද යන්න පරීක්ෂා කිරීමට ඔබේ බෝගවල ඡායාරූපයක් ගන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_one_title.setText("உங்கள் பயிர்களை ஆய்வு செய்யுங்கள்")
            view.on_board_one_dtl.setText("உங்கள் பயிர்கள் ஆரோக்கியமானவையா இல்லையா என்பதைச் சரிபார்க்க அவற்றைப் புகைப்படம் எடுக்கவும்")

        }

        val onboardViewPager = activity?.findViewById<ViewPager2>(R.id.onboard_viewPager)

        view.nextScreen1.setOnClickListener {

            onboardViewPager?.currentItem = 1

        }

        return view

    }
}