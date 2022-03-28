package com.example.planter.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.planter.R
import com.example.planter.databinding.FragmentCropsBinding
import kotlinx.android.synthetic.main.fragment_crops.view.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentCrops : Fragment(R.layout.fragment_crops) {

    private var fragmentCropsBinding: FragmentCropsBinding? = null
    private lateinit var LANGUAGE: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCropsBinding.bind(view)
        fragmentCropsBinding = binding

        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            binding.selectCropText.setText("බෝග තෝරන්න")
            binding.cat1Text.setText("ඇපල්")
            binding.cat2Text.setText("කෙසෙල්")
            binding.cat3Text.setText("චෙරි")
            binding.cat4Text.setText("ඉරිඟු")
            binding.cat5Text.setText("මිදි")
            binding.cat6Text.setText("දොඩම්")
            binding.cat7Text.setText("බෙල් පෙපර්")
            binding.cat8Text.setText("අර්තාපල්")
            binding.cat9Text.setText("සහල්")
            binding.cat10Text.setText("ස්ට්රෝබෙරි")
            binding.cat11Text.setText("තේ")
            binding.cat12Text.setText("තක්කාලි")


        } else if (LANGUAGE == "tamil") {

            binding.selectCropText.setText("பயிர்களைத் தேர்ந்தெடுக்கவும்")
            binding.cat1Text.setText("ஆப்பிள்")
            binding.cat2Text.setText("வாழை")
            binding.cat3Text.setText("செர்ரி")
            binding.cat4Text.setText("சோளம்")
            binding.cat5Text.setText("திராட்சை")
            binding.cat6Text.setText("ஆரஞ்சு")
            binding.cat7Text.setText("பெல் மிளகு")
            binding.cat8Text.setText("உருளைக்கிழங்கு")
            binding.cat9Text.setText("அரிசி")
            binding.cat10Text.setText("ஸ்ட்ராபெர்ரி")
            binding.cat11Text.setText("தேநீர்")
            binding.cat12Text.setText("தக்காளி")

        }

        binding.cat1.setOnClickListener {
            val cropType = "apple"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat2.setOnClickListener {
            val cropType = "banana"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat3.setOnClickListener {
            val cropType = "cherry"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat4.setOnClickListener {
            val cropType = "corn"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat5.setOnClickListener {
            val cropType = "grape"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat6.setOnClickListener {
            val cropType = "orange"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat7.setOnClickListener {
            val cropType = "bell_pepper"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat8.setOnClickListener {
            val cropType = "potato"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat9.setOnClickListener {
            val cropType = "rice"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat10.setOnClickListener {
            val cropType = "strawberry"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat11.setOnClickListener {
            val cropType = "tea"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }

        binding.cat12.setOnClickListener {
            val cropType = "tomato"
            findNavController().navigate(
                FragmentCropsDirections.actionFragmentCropsToFragmentLevel(
                    cropType
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCropsBinding = null
    }
}