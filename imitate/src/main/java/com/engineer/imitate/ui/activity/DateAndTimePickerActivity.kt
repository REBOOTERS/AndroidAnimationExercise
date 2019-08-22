package com.engineer.imitate.ui.activity

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import kotlinx.android.synthetic.main.activity_date_and_time_picker.*
import java.util.*


class DateAndTimePickerActivity : AppCompatActivity() {

    private var button: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.engineer.imitate.R.layout.activity_date_and_time_picker)


        select.setOnClickListener {
            val pvTime = TimePickerBuilder(this@DateAndTimePickerActivity,
                OnTimeSelectListener { date, v -> }).build()
            pvTime.show()
        }

        testStandLib()

        test("aaa") {
            println("result is $it")
        }

        test("zzzz") {
            print(it)
        }


        val dd = test("22"){}
            .hashCode()




        val te = test1("z") {
            println()
        }

        test1("2",{})

        val result = test2("111") {
            "reeee"
        }


    }

    fun test(value: String, block: (String) -> Unit) {
        block.invoke(value.toUpperCase(Locale.CHINA))
    }

    fun test1(value: String, block: () -> Unit) {
        block.invoke()
    }

    fun test2(value: String, block: () -> String):String {
         return block.invoke()
    }

    fun testStandLib() {
        button?.let {
            // 必须使用 it
            it.performClick()
        }

        val value = select.let {
            it.performClick()
            1
        }

        val value1 = select.also {
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

        button?.apply {
            // 整体作用功能和run函数很像，
            // 唯一不同点就是它返回的值是对象本身，
            // 而run函数是一个闭包形式返回，返回的是最后一行的值

            // 可以省略参数

            performClick()
        }
    }
}
