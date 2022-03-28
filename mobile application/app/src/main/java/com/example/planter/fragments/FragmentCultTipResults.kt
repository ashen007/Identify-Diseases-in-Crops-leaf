package com.example.planter.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.planter.R
import com.example.planter.databinding.FragmentCultTipResultsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class FragmentCultTipResults : Fragment(R.layout.fragment_cult_tip_results) {

    private var fragmentCultTipResults: FragmentCultTipResultsBinding? = null
    private val queries by navArgs<FragmentCultTipResultsArgs>()
    private lateinit var database: DatabaseReference
    private lateinit var LANGUAGE: String

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCultTipResultsBinding.bind(view)
        val crop = queries.cropType
        val tip = queries.tipType
        val sharedPref = requireActivity().getSharedPreferences("langChoice", Context.MODE_PRIVATE)

        LANGUAGE = sharedPref.getString("language", "english").toString()
        fragmentCultTipResults = binding

        if (tip == "fertilizers") {

            binding.tipBanner.setImageResource(R.drawable.add_fertilizers)

        } else if (tip == "harvesting") {

            binding.tipBanner.setImageResource(R.drawable.haevesting_grape)

        } else if (tip == "sowing") {

            binding.tipBanner.setImageResource(R.drawable.sowing)

        } else if (tip == "prep_soil") {

            binding.tipBanner.setImageResource(R.drawable.soil_preparation)

        } else if (tip == "irrigation") {

            binding.tipBanner.setImageResource(R.drawable.watering_plants)

        }

        if (LANGUAGE == "english") {
            database = FirebaseDatabase.getInstance().getReference("english/cultTips")

            database.child(crop).get().addOnSuccessListener {
                val cropReference = it.child(tip)

                if ((tip == "fertilizers") || (tip == "harvesting") || (tip == "sowing")) {
                    val resultList: MutableList<String> = ArrayList()

                    for (element in cropReference.children) {
                        resultList.add(element.value.toString())
                    }

                    binding.tipResult.text = resultList.joinToString("\n\n\u25cf ", "\u25CF ")

                } else {
                    val results = cropReference.value.toString()

                    binding.tipResult.text = results
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

        } else if (LANGUAGE == "sinhala") {
            database = FirebaseDatabase.getInstance().getReference("sinhala/cultTips")

            database.child(crop).get().addOnSuccessListener {
                val cropReference = it.child(tip)

                if ((tip == "fertilizers") || (tip == "harvesting") || (tip == "sowing")) {
                    val resultList: MutableList<String> = ArrayList()

                    for (element in cropReference.children) {
                        resultList.add(element.value.toString())
                    }

                    binding.tipResult.text = resultList.joinToString("\n\n\u25cf ", "\u25CF ")

                } else {
                    val results = cropReference.value.toString()

                    binding.tipResult.text = results
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

        } else if (LANGUAGE == "tamil") {
            database = FirebaseDatabase.getInstance().getReference("tamil/cultTips")

            database.child(crop).get().addOnSuccessListener {
                val cropReference = it.child(tip)

                if ((tip == "fertilizers") || (tip == "harvesting") || (tip == "sowing")) {
                    val resultList: MutableList<String> = ArrayList()

                    for (element in cropReference.children) {
                        resultList.add(element.value.toString())
                    }

                    binding.tipResult.text = resultList.joinToString("\n\n\u25cf ", "\u25CF ")

                } else {
                    val results = cropReference.value.toString()

                    binding.tipResult.text = results
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCultTipResults = null
    }

}