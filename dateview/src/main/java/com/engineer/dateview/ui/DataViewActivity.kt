package com.engineer.dateview.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.engineer.dateview.R
import com.engineer.dateview.api.DataView
import com.engineer.dateview.internal.AutoSchedulerTransformer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_data_view.*


internal class DataViewActivity : AppCompatActivity(), OnChartValueSelectedListener {

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("CheckResult")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        bar.setOnChartValueSelectedListener(this)


        DataView.getRepository().all
                .compose(AutoSchedulerTransformer())
                .subscribe({
                    Log.e("ui", "size==${it.size}")

                }, {
                    it.printStackTrace()
                })
    }
}
