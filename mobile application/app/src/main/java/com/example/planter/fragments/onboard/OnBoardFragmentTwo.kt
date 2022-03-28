package com.example.planter.fragments.onboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board_one.view.*
import kotlinx.android.synthetic.main.fragment_on_board_two.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentTwo.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentTwo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board_two, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_two_title.setText("ඔබේ භෝග වල ඇති වැරැද්ද කුමක්ද?")
            view.on_board_two_dtl.setText("වඩාත් ඵලදායී රෝග පාලනය සඳහා ඔබේ බෝගවලට බලපෑ රෝග පිළිබඳ වැඩි දැනුමක් ලබා ගන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_two_title.setText("உங்கள் பயிர்களில் என்ன தவறு?")
            view.on_board_two_dtl.setText("மிகவும் பயனுள்ள நோயைக் கட்டுப்படுத்த உங்கள் பயிர்களுக்கு ஏற்படும் நோய்களைப் பற்றிய கூடுதல் அறிவைப் பெறுங்கள்")

        }

        val onboardViewPager = activity?.findViewById<ViewPager2>(R.id.onboard_viewPager)

        view.nextScreen2.setOnClickListener {

            onboardViewPager?.currentItem = 2

        }

        return view

    }
}