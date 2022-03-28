package com.example.planter.fragments.onboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board_five.view.*
import kotlinx.android.synthetic.main.fragment_on_board_one.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentFive.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentFive : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board_five, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_five_title.setText("ඔබේ අස්වැන්න වැඩි කර ගන්න")
            view.on_board_five_dtl.setText("අපගේ වගා ඉඟි සමඟ ඔබේ වගා දැනුම වැඩි දියුණු කර ගන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_five_title.setText("உங்கள் விளைச்சலை அதிகரிக்கவும்")
            view.on_board_five_dtl.setText("எங்கள் சாகுபடி குறிப்புகள் மூலம் உங்கள் சாகுபடி அறிவை மேம்படுத்தவும்")

        }

        val onboardViewPager = activity?.findViewById<ViewPager2>(R.id.onboard_viewPager)

        view.nextScreen5.setOnClickListener {

            onboardViewPager?.currentItem = 5

        }

        return view

    }

}