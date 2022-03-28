package com.example.planter.fragments.onboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board_final.view.*
import kotlinx.android.synthetic.main.fragment_on_board_one.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragmentFinal.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragmentFinal : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board_final, container, false)
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        val LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            view.on_board_final_dtl.setText("ඔබේ වගාව \nඋපදේශක")
            view.onboard_end.setText("ආරම්භ කරන්න")

        } else if (LANGUAGE == "tamil") {

            view.on_board_final_dtl.setText("உங்கள் சாகுபடி \nஆலோசகர்")
            view.onboard_end.setText("தொடங்குங்கள்")

        }

        view.onboard_end.setOnClickListener {
            findNavController().navigate(OnBoardFragmentDirections.actionOnBoardFragmentToMainActivity())
            onBoardingComplete()
            requireActivity().finish()
        }

        return view
    }

    private fun onBoardingComplete() {
        val sharedPref =
            requireActivity().getSharedPreferences("isOnBoardingCompleted", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.apply {
            editor.putBoolean("isOnBoardingCompleted", true)
            apply()
        }
    }
}