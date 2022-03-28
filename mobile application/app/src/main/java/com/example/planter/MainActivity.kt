package com.example.planter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.planter.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private val IMAGE_MEAN = 0
    private val IMAGE_STD = 255.0f
    private val MAX_RESULTS = 1
    private val THRESHOLD = 0.1f
    private lateinit var LANGUAGE: String
    private lateinit var savedFileName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("langChoice", Context.MODE_PRIVATE)
        LANGUAGE = sharedPref.getString("language", "english").toString()
        auth = FirebaseAuth.getInstance()

        singInAsAnonymous()

        if (LANGUAGE == "sinhala") {
            binding.mainCaution.setText(
                "Planter අවධානය යොමු කරන්නේ බෝග පත්\u200Dර රෝග හඳුනා ගැනීම සඳහා " +
                        "පමණි. Planter වෙනත් කිසිම ආකාරයක වස්තුවක් හඳුනාගත නොහැක. කරුණාකර පහත ශාකවල පත්\u200Dර " +
                        "රූප ලබා දෙන්න. ඇපල්, කෙසෙල්, චෙරි, ඉරිඟු, මිදි, දොඩම්, බෙල් පෙපර්, අර්තාපල්, සහල්, ස්ට්රෝබෙරි, තේ, තක්කාලි."
            )
            binding.detectHist.setText("පෙර හඳුනාගැනීම්")
            binding.addPhotoText.setText("පින්තූර එකතු කරන්න")
            binding.takePhotoText.setText("පින්තුරයක් ගන්න")
            binding.cultivationTipText.setText("වගා ඉඟි")
        } else if (LANGUAGE == "tamil") {
            binding.mainCaution.setText(
                "Planter பயிர் இலை நோய்களைக் கண்டறிவதில் மட்டுமே கவனம் செலுத்தப்படுகிறது. " +
                        "நடுபவர் வேறு எந்த வகையான பொருளையும் அடையாளம் காண முடியாது. " +
                        "பின்வரும் தாவரங்களின் இலைப் படங்களை வழங்கவும். ஆப்பிள்கள், " +
                        "வாழைப்பழங்கள், செர்ரிகள், சோளம், திராட்சை, ஆரஞ்சு, மணி " +
                        "மிளகுத்தூள், உருளைக்கிழங்கு, அரிசி, ஸ்ட்ராபெர்ரி, தேநீர், தக்காளி."
            )
            binding.detectHist.setText("முந்தைய கண்டறிதல்கள்")
            binding.addPhotoText.setText("படத்தைச் சேர்க்கவும்")
            binding.takePhotoText.setText("படம் எடு")
            binding.cultivationTipText.setText("சாகுபடி குறிப்புகள்")
        }

        supportActionBar?.hide()

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            goToResultsPage(it.name)
        }

        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            val isPrivate = true

            val fileName = "isPalntLabels.txt"
            val labelList =
                application.assets.open(fileName).bufferedReader().useLines { it.toList() }
            val result = Array(1) { FloatArray(labelList.size) }

            if (it != null) {
                val scaled = it.let { it1 -> Bitmap.createScaledBitmap(it1, 72, 72, false) }
                val byteBuffer = scaled?.let { it1 -> convertBitmapToByteBuffer(it1) }

                val options = Interpreter.Options()
                options.setNumThreads(5)
                options.setUseNNAPI(true)
                val interpreter = Interpreter(loadModelFile("Plant_or_not.tflite"), options)

                interpreter.run(byteBuffer, result)
                val preds = getSortedResult(result, labelList)

                val isPlant = preds[0].title

                if (isPrivate) {
                    if (isPlant == "yes") {
                        val isSavedSuccessfully =
                            it.let { it1 ->
                                savePhotoToInternalStorage(
                                    UUID.randomUUID().toString(),
                                    it1
                                )
                            }
                        if (isSavedSuccessfully == true) {
                            loadPhotosFromInternalStorageIntoRecyclerView()

                            Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "No plant detected. take new photo.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }

        val loadPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
            savedFileName = UUID.randomUUID().toString()

            val bitmap: Bitmap

            try {
                if (it != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                    val fileName = "isPalntLabels.txt"
                    val labelList =
                        application.assets.open(fileName).bufferedReader().useLines { it.toList() }

                    val result = Array(1) { FloatArray(labelList.size) }
                    val scaled = it.let { it1 -> Bitmap.createScaledBitmap(bitmap, 72, 72, false) }
                    val byteBuffer = scaled?.let { it1 -> convertBitmapToByteBuffer(it1) }
                    val options = Interpreter.Options()
                    options.setNumThreads(5)
                    options.setUseNNAPI(true)
                    val interpreter = Interpreter(loadModelFile("Plant_or_not.tflite"), options)

                    interpreter.run(byteBuffer, result)
                    val preds = getSortedResult(result, labelList)

                    val isPlant = preds[0].title

                    if (isPlant == "yes") {
                        val isSavedSuccessfully =
                            it.let { it1 -> savePhotoToInternalStorage(savedFileName, bitmap) }

                        if (isSavedSuccessfully == true) {
                            loadPhotosFromInternalStorageIntoRecyclerView()

                            Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "No plant detected. take new photo.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        binding.takePhoto.setOnClickListener {
            takePhoto.launch()
        }

        binding.uploadPhoto.setOnClickListener {
            loadPhoto.launch("image/*")
        }

        binding.cultTips.setOnClickListener {
            goToCategoryPage()
        }

        setupInternalStorageRecyclerView()
        loadPhotosFromInternalStorageIntoRecyclerView()
    }

    private fun singInAsAnonymous() {
        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success
                Log.d(TAG, "signInAnonymously:success")

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInAnonymously:failure", task.exception)
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun uploadToFireStorage(bitmap: Bitmap) {
        // show loading dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)

        val storageRef = FirebaseStorage.getInstance().getReference("user_taken_img/$filename.jpg")

        val baos = ByteArrayOutputStream()
        val scaled_bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false)
        scaled_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            if (progressDialog.isShowing) progressDialog.dismiss()
        }
    }

    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = StaggeredGridLayoutManager(4, RecyclerView.VERTICAL)
    }

    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = getAppSpecificStorageDir()
            internalStoragePhotoAdapter.submitList(photos)
        }
    }

    private fun goToResultsPage(filename: String) {
        val intent = Intent(this, DetailsActivity::class.java).putExtra("filename", filename)
        startActivity(intent)
    }

    private fun goToCategoryPage() {
        val intent = Intent(this, CategoryActivity::class.java)
        startActivity(intent)
    }

    private fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return try {
            deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        return try {
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            uploadToFireStorage(bmp)
            goToResultsPage("$filename.jpg")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun getAppSpecificStorageDir(): List<InternalStoragePhoto> {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        return withContext(Dispatchers.IO) {
            val files = filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
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
    ): List<IsPlant> {

        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<IsPlant> { (_, _, confidence1), (_, _, confidence2) ->
                java.lang.Float.compare(confidence1, confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD) {
                pq.add(
                    IsPlant(
                        "" + i,
                        if (labelList.size > i) labelList[i] else "Unknown", confidence
                    )
                )
            }
        }

        val recognitions = ArrayList<IsPlant>()
        val recognitionsSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }

        return recognitions
    }
}