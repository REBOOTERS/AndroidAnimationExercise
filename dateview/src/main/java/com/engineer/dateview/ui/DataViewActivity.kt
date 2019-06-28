package com.engineer.dateview.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.engineer.dateview.R
import com.engineer.dateview.api.DataView
import com.engineer.dateview.internal.AutoSchedulerTransformer
import com.engineer.dateview.model.ActModel
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.StackedValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_data_view.*


internal class DataViewActivity : AppCompatActivity() {


    @SuppressLint("CheckResult")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)





        DataView.getRepository().all
                .compose(AutoSchedulerTransformer())
                .subscribe({
                    Log.e("ui", "size==${it.size}")

                    loadUI(it)

                }, {
                    it.printStackTrace()
                })
    }

    private fun loadUI(it: List<ActModel>) {
        val values = ArrayList<PieEntry>()

        for (i in 0..7) {
            val pie = PieEntry(i.toFloat())
            values.add(pie)
        }

//        for ((index, value) in it.withIndex()) {
//            val x = index.toFloat()
//            val y = floatArrayOf(
//                    value.onActivityCreateCount.toFloat(),
//                    value.onActivityStartedCount.toFloat(),
//                    value.onActivityResumedCount.toFloat(),
//                    value.onActivityPausedCount.toFloat(),
//                    value.onActivityStoppedCount.toFloat(),
//                    value.onActivitySaveInstanceStateCount.toFloat(),
//                    value.onActivityDestroyedCount.toFloat()
//            )
//            val bar = PieEntry(x, y)
//            values.add(bar)
//        }

        val barDataSet = PieDataSet(values, "this is first")
        barDataSet.colors = getColors()


        val barData = PieData(barDataSet)
        barData.setValueFormatter(StackedValueFormatter(false, "", 1))
        barData.setValueTextColor(Color.WHITE)
        bar.data = barData
    }

    private fun getColors(): List<Int> {

        val colors = ArrayList<Int>(7)
        colors.add(ColorTemplate.COLORFUL_COLORS[0])
        colors.add(ColorTemplate.COLORFUL_COLORS[1])
        colors.add(ColorTemplate.COLORFUL_COLORS[2])
        colors.add(ColorTemplate.COLORFUL_COLORS[3])
        colors.add(ColorTemplate.COLORFUL_COLORS[4])
        colors.add(ColorTemplate.VORDIPLOM_COLORS[0])
        colors.add(ColorTemplate.VORDIPLOM_COLORS[1])
        return colors
    }
}
