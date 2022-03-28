package com.example.planter.fragments.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planter.R
import kotlinx.android.synthetic.main.fragment_on_board.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [OnBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnBoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board, container, false)

        val fragmentList = arrayListOf<Fragment>(
            OnBoardFragmentOne(),
            OnBoardFragmentTwo(),
            OnBoardFragmentThree(),
            OnBoardFragmentFour(),
            OnBoardFragmentFive(),
            OnBoardFragmentFinal()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.onboard_viewPager.adapter = adapter

        return view
    }

}