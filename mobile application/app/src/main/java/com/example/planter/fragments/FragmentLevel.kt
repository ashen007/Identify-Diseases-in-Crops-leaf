package com.example.planter.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.planter.R
import com.example.planter.databinding.FragmentLevelBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class FragmentLevel : Fragment(R.layout.fragment_level) {

    private var fragmentLevelBinding: FragmentLevelBinding? = null
    private val cropType by navArgs<FragmentLevelArgs>()
    private lateinit var database: DatabaseReference
    private lateinit var LANGUAGE: String

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLevelBinding.bind(view)
        fragmentLevelBinding = binding

        val crop = cropType.cropType.toString()
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        LANGUAGE = sharedPref.getString("language", "english").toString()

        if (LANGUAGE == "sinhala") {

            binding.soilText.setText("පස සකස් කිරීම")
            binding.sowingText.setText("වැපිරීම")
            binding.fertText.setText("පොහොර සහ පොහොර එකතු කිරීම")
            binding.waterText.setText("ජල කළමනාකරණය")
            binding.harvText.setText("අස්වනු නෙලීම සහ ගබඩා කිරීම")

        } else if (LANGUAGE == "tamil") {

            binding.soilText.setText("மண் தயாரித்தல்")
            binding.sowingText.setText("விதைத்தல்")
            binding.fertText.setText("உரம் மற்றும் உரங்கள் சேர்த்தல்")
            binding.waterText.setText("நீர் மேலாண்மை")
            binding.harvText.setText("அறுவடை மற்றும் சேமிப்பு")

        }

        if (crop == "apple") {

            binding.bannerImg.setImageResource(R.drawable.ic_apple)
            binding.cropTitle.setText("Apple")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("ඇපල්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("ஆப்பிள்")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "banana") {

            binding.bannerImg.setImageResource(R.drawable.ic_banana)
            binding.cropTitle.setText("Banana")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("කෙසෙල්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("வாழை")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "cherry") {

            binding.bannerImg.setImageResource(R.drawable.ic_cherry)
            binding.cropTitle.setText("Cherry")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("චෙරි")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("செர்ரி")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "corn") {

            binding.bannerImg.setImageResource(R.drawable.ic_corn)
            binding.cropTitle.setText("Corn")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("ඉරිඟු")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("சோளம்")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "grape") {

            binding.bannerImg.setImageResource(R.drawable.ic_grape)
            binding.cropTitle.setText("Grape")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("මිදි")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("திராட்சை")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "orange") {

            binding.bannerImg.setImageResource(R.drawable.ic_orange)
            binding.cropTitle.setText("Orange")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("දොඩම්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("ஆரஞ்சு")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "bell_pepper") {

            binding.bannerImg.setImageResource(R.drawable.ic_bell_papper)
            binding.cropTitle.setText("Bell pepper")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("Bell බෙල් පෙපර්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("பெல் மிளகு")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "potato") {

            binding.bannerImg.setImageResource(R.drawable.ic_potato)
            binding.cropTitle.setText("Potato")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("අර්තාපල්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("உருளைக்கிழங்கு")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "rice") {

            binding.bannerImg.setImageResource(R.drawable.ic_rice)
            binding.cropTitle.setText("Rice")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("සහල්")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("அரிசி")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "strawberry") {

            binding.bannerImg.setImageResource(R.drawable.ic_strawberry)
            binding.cropTitle.setText("Strawberry")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("ස්ට්රෝබෙරි")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("ஸ்ட்ராபெர்ரி")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "tea") {

            binding.bannerImg.setImageResource(R.drawable.ic_tea)
            binding.cropTitle.setText("Tea")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("තේ")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("தேநீர்")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        } else if (crop == "tomato") {

            binding.bannerImg.setImageResource(R.drawable.ic_tomato)
            binding.cropTitle.setText("Tomato")

            if (LANGUAGE == "english") {
                setGenDataToViews(LANGUAGE, crop, binding)
            } else if (LANGUAGE == "sinhala") {

                binding.cropTitle.setText("තක්කාලි")
                setGenDataToViews(LANGUAGE, crop, binding)

            } else if (LANGUAGE == "tamil") {

                binding.cropTitle.setText("தக்காளி")
                setGenDataToViews(LANGUAGE, crop, binding)

            }

        }

        binding.prepSoil.setOnClickListener {
            val tipType = "prep_soil"
            findNavController().navigate(
                FragmentLevelDirections.actionFragmentLevelToFragmentCultTipResults(
                    crop,
                    tipType
                )
            )
        }

        binding.sowing.setOnClickListener {
            val tipType = "sowing"
            findNavController().navigate(
                FragmentLevelDirections.actionFragmentLevelToFragmentCultTipResults(
                    crop,
                    tipType
                )
            )
        }

        binding.fert.setOnClickListener {
            val tipType = "fertilizers"
            findNavController().navigate(
                FragmentLevelDirections.actionFragmentLevelToFragmentCultTipResults(
                    crop,
                    tipType
                )
            )
        }

        binding.watering.setOnClickListener {
            val tipType = "irrigation"
            findNavController().navigate(
                FragmentLevelDirections.actionFragmentLevelToFragmentCultTipResults(
                    crop,
                    tipType
                )
            )
        }

        binding.harvest.setOnClickListener {
            val tipType = "harvesting"
            findNavController().navigate(
                FragmentLevelDirections.actionFragmentLevelToFragmentCultTipResults(
                    crop,
                    tipType
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setGenDataToViews(lan: String, crop: String, binding: FragmentLevelBinding) {
        database = FirebaseDatabase.getInstance().getReference("$lan/cultTips")

        database.child(crop).get().addOnSuccessListener {
            val genDtlReference = it.child("genDtl")
            val type = genDtlReference.child("type")
            val sciName = genDtlReference.child("sciName")
            val duration = genDtlReference.child("duration")
            val variant = genDtlReference.child("variant")
            val space = genDtlReference.child("space")
            val comDiff = genDtlReference.child("comDiff")
            val criticalNeed = genDtlReference.child("criticalNeed")

            if (LANGUAGE == "sinhala") {

                binding.variantText.setText("වඩාත් පොදු ප්රභේද ගණන")
                binding.spaceText.setText("තනි පැලයකට අවශ්\u200Dය ඉඩ")
                binding.comChallengeText.setText("වඩාත් පොදු අභියෝගය")
                binding.criticNeedsText.setText("තීරණාත්මක අවශ්යතා")

            } else if (LANGUAGE == "tamil") {

                binding.variantText.setText("மிகவும் பொதுவான வகைகளின் எண்ணிக்கை")
                binding.spaceText.setText("ஒரு ஆலைக்கு தேவையான இடம்")
                binding.comChallengeText.setText("மிகவும் பொதுவான சவால்")
                binding.criticNeedsText.setText("முக்கியமான தேவைகள்")

            }

            binding.type.setText(type.value.toString())
            binding.sciName.setText(sciName.value.toString())
            binding.duration.setText(duration.value.toString())
            binding.variant.setText(variant.value.toString())
            binding.space.setText(space.value.toString())
            binding.comChallenge.setText(comDiff.value.toString())
            binding.criticNeeds.setText(criticalNeed.value.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentLevelBinding = null
    }
}