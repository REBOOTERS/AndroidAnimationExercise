package com.engineer.imitate.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.databinding.ActivityDateAndTimePickerBinding
import com.engineer.imitate.receivers.AlarmReceiver
import com.engineer.imitate.util.SysUtil
import java.util.Calendar


class DateAndTimePickerActivity : AppCompatActivity() {
    private val TAG = "picker"
    private var button: Button? = null
    private lateinit var receiver: AlarmReceiver
    private lateinit var viewBinding: ActivityDateAndTimePickerBinding

    companion object {
        val alarm_log = "alarm_log"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDateAndTimePickerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.go.setOnClickListener {
            val hour: Int
            val minute: Int
            if (SysUtil.isAndroidMOrLater()) {
                hour = viewBinding.timePicker.hour
                minute = viewBinding.timePicker.minute
            } else {
                hour = viewBinding.timePicker.currentHour
                minute = viewBinding.timePicker.currentMinute
            }

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            val intent = Intent(alarm_log)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            val alarm: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarm.setRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1000 * 120, pendingIntent
            )
        }

        viewBinding.go1.setOnClickListener {
            val intent = Intent(alarm_log)
            sendBroadcast(intent)
        }

        viewBinding.select.setOnClickListener {

        }

        testStandLib()


        registerReceiver()
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(alarm_log)
        receiver = AlarmReceiver()
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }


    fun testStandLib() {
        button?.let {
            // 必须使用 it
            it.performClick()
        }

        val value = viewBinding.select.let {
            it.performClick()
            1
        }

        val value1 = viewBinding.select.also {
            it.hashCode()
            //  also函数的结构实际上和let很像唯一的区别就是返回值的不一样，
            //  let是以闭包的形式返回，返回函数体内最后一行的值，
            //  如果最后一行为空就返回一个Unit类型的默认值。而also函数返回的则是传入对象的本身
        }


        val nonullValue = Paint()
        with(nonullValue) {
            // 参数就是 with 中的值，只是为了方便在后续 setxxx 的时候，可以少些点东西
            setColorFilter(PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.ADD))
        }

        with(button) {
            if (button != null) {
                button.hashCode()
            }
        }

        button.run {
            // 参数不是 it

            // 适用于let,with函数任何场景。
            // 因为run函数是let,with两个函数结合体，准确来说它弥补了let函数在函数体内必须使用it参数替代对象，
            // 在run函数中可以像with函数一样可以省略，直接访问实例的公有属性和方法，
            // 另一方面它弥补了with函数传入对象判空问题，在run函数中可以像let函数一样做判空处理

            button?.setText("111")
            1
        }

        run {
            println("")
        }

        val d = button?.apply {
            // 整体作用功能和run函数很像，
            // 唯一不同点就是它返回的值是对象本身，
            // 而run函数是一个闭包形式返回，返回的是最后一行的值

            // 可以省略参数

            performClick()
        }
    }
}
