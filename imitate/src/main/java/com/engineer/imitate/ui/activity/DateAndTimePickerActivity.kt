package com.engineer.imitate.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import kotlinx.android.synthetic.main.activity_date_and_time_picker.*


class DateAndTimePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.engineer.imitate.R.layout.activity_date_and_time_picker)


        select.setOnClickListener {
            val pvTime = TimePickerBuilder(this@DateAndTimePickerActivity,
                    OnTimeSelectListener { date, v -> }).build()
            pvTime.show()
        }


    }
}
