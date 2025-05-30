package com.engineer.ai

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.divyanshu.draw.widget.DrawView
import com.engineer.ai.util.DigitClassifier
import com.engineer.ai.util.toast

class DigitalClassificationActivity : AppCompatActivity() {
    private var drawView: DrawView? = null
    private var clearButton: Button? = null
    private var initButton: Button? = null
    private var predictedTextView: TextView? = null
    private var digitClassifier = DigitClassifier(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_digital_classification)

        // Setup view instances.
        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(70.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(Color.BLACK)
        clearButton = findViewById(R.id.clear_button)
        initButton = findViewById(R.id.init_model)
        predictedTextView = findViewById(R.id.predicted_text)

        // Setup clear drawing button.
        clearButton?.setOnClickListener {
            drawView?.clearCanvas()
            predictedTextView?.text = getString(R.string.prediction_text_placeholder)
        }

        // Setup classification trigger so that it classify after every stroke drew.
        drawView?.setOnTouchListener { _, event ->
            // As we have interrupted DrawView's touch event,
            // we first need to pass touch events through to the instance for the drawing to show up.
            drawView?.onTouchEvent(event)

            // Then if user finished a touch event, run classification
            if (event.action == MotionEvent.ACTION_UP) {
                classifyDrawing()
            }

            true
        }


        // Setup digit classifier.
        initButton?.setOnClickListener {
            digitClassifier.initialize {
                runOnUiThread { if (it) "init success".toast(this) else "init fail".toast(this) }
            }
        }

    }

    override fun onDestroy() {
        // Sync DigitClassifier instance lifecycle with MainActivity lifecycle,
        // and free up resources (e.g. TF Lite instance) once the activity is destroyed.
        digitClassifier.close()
        super.onDestroy()
    }

    private fun classifyDrawing() {
        val bitmap = drawView?.getBitmap()

        if ((bitmap != null) && (digitClassifier.isInitialized)) {
            digitClassifier.classifyAsync(bitmap)
                .addOnSuccessListener { resultText -> predictedTextView?.text = resultText }.addOnFailureListener { e ->
                    predictedTextView?.text = getString(
                        R.string.classification_error_message, e.localizedMessage
                    )
                    Log.e(TAG, "Error classifying drawing.", e)
                }
        }
    }

    companion object {
        private const val TAG = "DigitalClassification"
    }
}