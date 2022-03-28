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
import kotlinx.android.synthetic.main.fragment_on_board_three.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentThree.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentThree : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board_three, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_three_title.setText("රෝග පාලනය")
            view.on_board_three_dtl.setText("ඵලදායී කාබනික, කාබනික නොවන රෝග පාලන ක්රම සහ සංස්කෘතික භාවිතයන් ඉගෙන ගන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_three_title.setText("நோய் கட்டுப்பாடு")
            view.on_board_three_dtl.setText("பயனுள்ள கரிம, கரிம அல்லாத நோய்களைக் கட்டுப்படுத்தும் முறைகள் மற்றும் கலாச்சார நடைமுறைகளைக் கற்றுக்கொள்ளுங்கள்")

        }

        val onboardViewPager = activity?.findViewById<ViewPager2>(R.id.onboard_viewPager)

        view.nextScreen3.setOnClickListener {

            onboardViewPager?.currentItem = 3

        }

        return view

    }
}