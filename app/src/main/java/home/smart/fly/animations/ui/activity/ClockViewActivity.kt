package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_clock_view.*


class ClockViewActivity : AppCompatActivity() {

    private var pauseValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_view)


        set.setOnClickListener{
            clockView.setTime(Integer.parseInt(value.text.toString()))
        }



        start.setOnClickListener {
            clockView.start()
            pauseValue = false
        }

        pause.setOnClickListener {
            clockView.pause()
            if (pauseValue) {
                pause.text = "PAUSE"
            } else {
                pause.text = "RESUME"
            }
            pauseValue = !pauseValue;
        }

        stop.setOnClickListener { clockView.stop() }
    }
}
