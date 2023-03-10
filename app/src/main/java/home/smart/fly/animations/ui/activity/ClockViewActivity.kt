package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import home.smart.fly.animations.customview.ClockView


class ClockViewActivity : AppCompatActivity() {

    private var pauseValue = false
    private lateinit var clockView: ClockView
    private lateinit var value: EditText
    private lateinit var pause: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_view)

        clockView = findViewById(R.id.clockView)
        value = findViewById(R.id.value)
        pause = findViewById(R.id.pause)
        findViewById<View>(R.id.set).setOnClickListener {
            clockView.setTime(Integer.parseInt(value.text.toString()))
        }



        findViewById<View>(R.id.start).setOnClickListener {
            clockView.start()
            pauseValue = false
        }

        findViewById<View>(R.id.pause).setOnClickListener {
            clockView.pause()
            if (pauseValue) {
                pause.text = "PAUSE"
            } else {
                pause.text = "RESUME"
            }
            pauseValue = !pauseValue;
        }

        findViewById<View>(R.id.stop).setOnClickListener { clockView.stop() }
    }
}
