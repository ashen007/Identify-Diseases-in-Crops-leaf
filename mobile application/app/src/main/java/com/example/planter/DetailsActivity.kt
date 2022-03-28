package com.example.planter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.planter.databinding.ActivityDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DetailsActivity : AppCompatActivity() {
    private val IMAGE_MEAN = 0
    private val IMAGE_STD = 255.0f
    private val MAX_RESULTS = 1
    private val THRESHOLD = 0.1f
    private lateinit var LANGUAGE: String
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var database: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        LANGUAGE = sharedPref.getString("language", "english").toString()

        val actionBar = supportActionBar

        if (LANGUAGE == "english") {
            if (actionBar != null) {
                actionBar.title = "Similar Results"
            }

        } else if (LANGUAGE == "sinhala") {
            if (actionBar != null) {
                actionBar.title = "සමාන ප්රතිඵල"
            }

            binding.topDtl.setText("විශ්වාසය")
            binding.symptoms.setText("රෝග ලක්ෂණ")
            binding.moreInfoText.setText("වැඩි විස්තර")
            binding.recommendationText.setText("නිර්දේශ")
            binding.textView2.setText("සාමාන්\u200Dය රෝග කළමනාකරණය")
            binding.textView5.setText(
                "කිසියම් ක්\u200Dරියාමාර්ගයක් ගැනීමට පෙර, මෙම නිර්දේශිත රෝග විනිශ්චය ඔබේ " +
                        "ගැටලුවට ගැලපෙන බවට වග බලා ගැනීමට රෝග ලක්ෂණ කියවන්න."
            )
            binding.orgConText.setText("කාබනික පාලනය")
            binding.chemConText.setText("රසායනික පාලනය")
            binding.textView6.setText(
                "අවවාදයයි: විශ්වාසනීය මට්ටම සැලකිය යුතු ලෙස අඩු නම්, වෙනත් කෝණයකින් නව " +
                        "රූපයක් ගැනීම හෝ තනි පත්\u200Dරයක වැඩි අවධානයක් යොමු කර ඡායාරූපයක් ගැනීමට උත්සාහ කිරීම හොඳය."
            )

        } else if (LANGUAGE == "tamil") {
            if (actionBar != null) {
                actionBar.title = "இதே போன்ற முடிவுகள்"
            }

            binding.topDtl.setText("நம்பிக்கை")
            binding.symptoms.setText("அறிகுறிகள்")
            binding.moreInfoText.setText("மேலும் தகவல்")
            binding.recommendationText.setText("பரிந்துரைகள்")
            binding.textView2.setText("பொது நோய் மேலாண்மை")
            binding.textView5.setText(
                "எந்த நடவடிக்கையும் எடுப்பதற்கு முன், இந்த " +
                        "பரிந்துரைக்கப்பட்ட நோயறிதல் உங்கள் பிரச்சனைக்கு பொருந்துமா என்பதை " +
                        "உறுதிப்படுத்த அறிகுறிகளைப் படிக்கவும்."
            )
            binding.orgConText.setText("கரிம கட்டுப்பாடு")
            binding.chemConText.setText("இரசாயன கட்டுப்பாடு")
            binding.textView6.setText(
                "எச்சரிக்கை: நம்பிக்கை நிலை குறிப்பிடத்தக்க வகையில் " +
                        "குறைவாக இருந்தால், வெவ்வேறு கோணங்களில் இருந்து புதிய படங்களை " +
                        "எடுப்பது நல்லது அல்லது ஒற்றை இலையில் அதிக கவனம் செலுத்தும் " +
                        "புகைப்படங்களை எடுக்க முயற்சிப்பது நல்லது."
            )

        }

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val filename = intent.getStringExtra("filename")
        val scaledBmp = filename?.let { selectFromHistory(it) }
        val byteBuffer = scaledBmp?.let { convertBitmapToByteBuffer(it) }
        val fileName = "labels.txt"
        val labelList = application.assets.open(fileName).bufferedReader().useLines { it.toList() }
        val result = Array(1) { FloatArray(labelList.size) }

        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        val interpreter = Interpreter(loadModelFile("DeepWideNet_BE_WND.tflite"), options)

        interpreter.run(byteBuffer, result)
        val preds = getSortedResult(result, labelList)
        val confidence = preds.get(0).confidence * 100
        val number3digits: Double = Math.round(confidence * 1000.0) / 1000.0
        val number2digits: Double = Math.round(number3digits * 100.0) / 100.0
        val solution: Double = Math.round(number2digits * 10.0) / 10.0

        binding.topClass.setText(preds.get(0).title)
        binding.topDtl.setText("confidence: ${solution.toString()}%")

        readDataFromDataBase(preds[0].title)
        listFiles(preds[0].title)

    }

    @SuppressLint("SetTextI18n")
    private fun readDataFromDataBase(key: String) {

        // read data if key doesn't have healthy
        if (!key.contains("healthy", ignoreCase = true)) {

            if (LANGUAGE == "english") {
                database = FirebaseDatabase.getInstance().getReference("english/disease")

                database.child(key).get().addOnSuccessListener {
                    if (it.exists()) {
                        val symptoms = it.child("symptoms")
                        val moreInfo = it.child("moreInfo")
                        val recommend = it.child("recommend")

                        // Result will be holded Here
                        val lst: MutableList<String> = ArrayList()
                        val genMngLst: MutableList<String> = ArrayList()
                        val strgLst: MutableList<String> = ArrayList()
                        val organic: MutableList<String> = ArrayList()
                        val chemical: MutableList<String> = ArrayList()

                        //add result into array list
                        for (dsp in symptoms.children) {
                            lst.add(dsp.value.toString())
                        }

                        // setting results into text views
                        binding.mltvSymp.setText(lst.joinToString(" "))
                        binding.tvInfo1.setText("Agent: " + moreInfo.child("agent").value.toString())
                        binding.tvInfo2.setText("Favorable environment: " + moreInfo.child("favEnv").value.toString())
                        binding.tvInfo3.setText("Hosts: " + moreInfo.child("hosts").value.toString())
                        binding.tvInfo4.setText("Lifecycle: " + moreInfo.child("lifecycle").value.toString())

                        for (dsp in recommend.child("genDiseMng").children) {
                            genMngLst.add(dsp.value.toString())
                        }

                        binding.tvRecIntro.setText(recommend.child("intro").value.toString())
                        binding.tvRecGenMng.setText(genMngLst.joinToString(" "))

                        for (dsp in recommend.child("strategy").children) {
                            strgLst.add(dsp.value.toString())
                        }

                        binding.tvRecStrategy.setText(strgLst.joinToString(" "))

                        for (dsp in recommend.child("organic").children) {
                            organic.add(dsp.value.toString())
                        }

                        for (dsp in recommend.child("chemical").children) {
                            chemical.add(dsp.value.toString())
                        }

                        binding.tvOrgDtl2.setText(organic.joinToString("\n"))
                        binding.tvChemDt1.setText(chemical.joinToString("\n"))

                    } else {
                        // disease on in database
                        Toast.makeText(
                            this, "Disease has no records database.${it}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
            } else if (LANGUAGE == "sinhala") {
                database = FirebaseDatabase.getInstance().getReference("sinhala/disease")

                database.child(key).get().addOnSuccessListener {
                    if (it.exists()) {
                        val symptoms = it.child("symptoms")
                        val moreInfo = it.child("moreInfo")
                        val recommend = it.child("recommend")

                        // Result will be holded Here
                        val lst: MutableList<String> = ArrayList()
                        val genMngLst: MutableList<String> = ArrayList()
                        val strgLst: MutableList<String> = ArrayList()
                        val organic: MutableList<String> = ArrayList()
                        val chemical: MutableList<String> = ArrayList()

                        //add result into array list
                        for (dsp in symptoms.children) {
                            lst.add(dsp.value.toString())
                        }

                        // setting results into text views
                        binding.mltvSymp.setText(lst.joinToString(" "))
                        binding.tvInfo1.setText("නියෝජිතයා: " + moreInfo.child("agent").value.toString())
                        binding.tvInfo2.setText("හිතකර පරිසරය: " + moreInfo.child("favEnv").value.toString())
                        binding.tvInfo3.setText("සත්කාරක: " + moreInfo.child("hosts").value.toString())
                        binding.tvInfo4.setText("ජීවන චක්රය: " + moreInfo.child("lifecycle").value.toString())

                        for (dsp in recommend.child("genDiseMng").children) {
                            genMngLst.add(dsp.value.toString())
                        }

                        binding.tvRecIntro.setText(recommend.child("intro").value.toString())
                        binding.tvRecGenMng.setText(genMngLst.joinToString(" "))

                        for (dsp in recommend.child("strategy").children) {
                            strgLst.add(dsp.value.toString())
                        }

                        binding.tvRecStrategy.setText(strgLst.joinToString(" "))

                        for (dsp in recommend.child("organic").children) {
                            organic.add(dsp.value.toString())
                        }

                        for (dsp in recommend.child("chemical").children) {
                            chemical.add(dsp.value.toString())
                        }

                        binding.tvOrgDtl2.setText(organic.joinToString("\n"))
                        binding.tvChemDt1.setText(chemical.joinToString("\n"))

                    } else {
                        // disease on in database
                        Toast.makeText(
                            this, "Disease has no records database.${it}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }

            } else if (LANGUAGE == "tamil") {
                database = FirebaseDatabase.getInstance().getReference("tamil/disease")

                database.child(key).get().addOnSuccessListener {
                    if (it.exists()) {
                        val symptoms = it.child("symptoms")
                        val moreInfo = it.child("moreInfo")
                        val recommend = it.child("recommend")

                        // Result will be holded Here
                        val lst: MutableList<String> = ArrayList()
                        val genMngLst: MutableList<String> = ArrayList()
                        val strgLst: MutableList<String> = ArrayList()
                        val organic: MutableList<String> = ArrayList()
                        val chemical: MutableList<String> = ArrayList()

                        //add result into array list
                        for (dsp in symptoms.children) {
                            lst.add(dsp.value.toString())
                        }

                        // setting results into text views
                        binding.mltvSymp.setText(lst.joinToString(" "))
                        binding.tvInfo1.setText("முகவர்: " + moreInfo.child("agent").value.toString())
                        binding.tvInfo2.setText("சாதகமான சூழல்: " + moreInfo.child("favEnv").value.toString())
                        binding.tvInfo3.setText("புரவலர்கள்: " + moreInfo.child("hosts").value.toString())
                        binding.tvInfo4.setText("வாழ்க்கைச் சுழற்சி: " + moreInfo.child("lifecycle").value.toString())

                        for (dsp in recommend.child("genDiseMng").children) {
                            genMngLst.add(dsp.value.toString())
                        }

                        binding.tvRecIntro.setText(recommend.child("intro").value.toString())
                        binding.tvRecGenMng.setText(genMngLst.joinToString(" "))

                        for (dsp in recommend.child("strategy").children) {
                            strgLst.add(dsp.value.toString())
                        }

                        binding.tvRecStrategy.setText(strgLst.joinToString(" "))

                        for (dsp in recommend.child("organic").children) {
                            organic.add(dsp.value.toString())
                        }

                        for (dsp in recommend.child("chemical").children) {
                            chemical.add(dsp.value.toString())
                        }

                        binding.tvOrgDtl2.setText(organic.joinToString("\n"))
                        binding.tvChemDt1.setText(chemical.joinToString("\n"))

                    } else {
                        // disease on in database
                        Toast.makeText(
                            this, "Disease has no records database.${it}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }

            }

        } else {

            binding.parentSymp.visibility = View.GONE
            binding.parentMore.visibility = View.GONE
            binding.parentRec.visibility = View.GONE
            binding.parentOrganic.visibility = View.GONE
            binding.parentChem.visibility = View.GONE
            binding.tvActionCaution.visibility = View.VISIBLE
            binding.tvHealthyCaution.visibility = View.GONE

            Toast.makeText(
                this, "This is healthy plant.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("WrongConstant")
    private fun listFiles(key: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            if (!key.contains("healthy", ignoreCase = true)) {
                // create reference to folder
                val storage = FirebaseStorage.getInstance()
                val images = storage.reference.child("example_disease_img/$key/").listAll().await()
                val imageUrls = mutableListOf<String>()

                for (image in images.items) {
                    val url = image.downloadUrl.await()
                    imageUrls.add(url.toString())
                }

                withContext(Dispatchers.Main) {
                    val imageAdapter = ImageAdapter(imageUrls)
                    rvImages.apply {
                        adapter = imageAdapter
                        layoutManager = LinearLayoutManager(
                            this@DetailsActivity,
                            LinearLayout.HORIZONTAL, false
                        )
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@DetailsActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadModelFile(modelPath: String): MappedByteBuffer {
        val fileDescriptor = application.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun selectFromHistory(filename: String): Bitmap {
        val directory = filesDir.absolutePath
        val path = "$directory/$filename"
        val selectedImage = BitmapFactory.decodeFile(path)
        val scaled = Bitmap.createScaledBitmap(selectedImage, 72, 72, false)

        binding.ivPredPhoto.setImageBitmap(selectedImage)

        return scaled
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 72 * 72 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(72 * 72)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until 72) {
            for (j in 0 until 72) {
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16) and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
            }
        }
        return byteBuffer
    }

    // get sorted results to show
    private fun getSortedResult(
        labelProbArray: Array<FloatArray>, labelList: List<String>
    ): List<Recognition> {

        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<Recognition> { (_, _, confidence1), (_, _, confidence2) ->
                java.lang.Float.compare(confidence1, confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD) {
                pq.add(
                    Recognition(
                        "" + i,
                        if (labelList.size > i) labelList[i] else "Unknown", confidence
                    )
                )
            }
        }

        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }

        return recognitions
    }
}