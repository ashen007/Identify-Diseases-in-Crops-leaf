package com.example.planter.fragments.onboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board_four.view.*
import kotlinx.android.synthetic.main.fragment_on_board_one.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentFour.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentFour : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board_four, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_four_title.setText("නියම වේලාවට අස්වැන්න නෙලන්න")
            view.on_board_four_dtl.setText("ශාක වලට හානි නොකර නියම වේලාවට අස්වැන්න නෙලන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_four_title.setText("சரியான நேரத்தில் அறுவடை செய்யுங்கள்")
            view.on_board_four_dtl.setText("செடிகளுக்கு சேதம் ஏற்படாமல் சரியான நேரத்தில் அறுவடை செய்யலாம்")

        }

        val onboardViewPager = activity?.findViewById<ViewPager2>(R.id.onboard_viewPager)

        view.nextScreen4.setOnClickListener {

            onboardViewPager?.currentItem = 4

        }

        return view

    }
}