package com.example.ildd

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.ildd.ml.VggnetBeWa
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.Comparator
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private val IMAGE_MEAN = 0
    private val IMAGE_STD = 255.0f
    private val MAX_RESULTS = 4
    private val THRESHOLD = 0.4f
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    var labelList = loadLabelList(application.assets, "labels.txt")

    // output creation function
    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0f
    ) {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence"
        }
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }

    }

    // convert bitmap to float array and scale to 0-1 range
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 56 * 56 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(56 * 56)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until 56) {
            for (j in 0 until 56) {
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16) and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
                byteBuffer.putFloat((((input and 0xFF) - IMAGE_MEAN) / IMAGE_STD))
            }
        }
        return byteBuffer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        var load_img: Button = findViewById(R.id.btn_load_image)
        var tv: TextView = findViewById(R.id.tv_output)

        // fire actions when click on load image button
        load_img.setOnClickListener(View.OnClickListener {

            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)

        })

        var predict: Button = findViewById(R.id.btn_predict)

        // fire actions when click on prediction button
        predict.setOnClickListener(View.OnClickListener {

            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 56, 56, false)
            val model = VggnetBeWa.newInstance(this)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 56, 56, 3), DataType.FLOAT32)
            var byteBuffer = convertBitmapToByteBuffer(resized)

            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            bestPreds = getSortedResult()

            tv.setText()

            // Releases model resources if no longer used.
            model.close()

        })

    }

    // read image from file system
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageView.setImageURI(data?.data)

        var uri: Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

    }

    // get sorted results to show
    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<MainActivity.Recognition> {
        Log.d(
            "Classifier",
            "List Size:(%d,%d,%d)".format(
                labelProbArray.size, labelProbArray[0].size, labelList.size
            )
        )

        val pq = PriorityQueue(
            MAX_RESULTS,
            Comparator<MainActivity.Recognition> { (_, _, confidence1), (_, _, confidence2) ->
                java.lang.Float.compare(confidence1, confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= THRESHOLD) {
                pq.add(
                    MainActivity.Recognition(
                        "" + i,
                        if (labelList.size > i) labelList[i] else "Unknown", confidence
                    )
                )
            }
        }

        val recognitions = ArrayList<MainActivity.Recognition>()
        val recognitionsSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }

        return recognitions
    }

}