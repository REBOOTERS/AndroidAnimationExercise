package com.engineer.dateview.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bin.david.form.core.SmartTable
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
        val view: SmartTable<ActModel> = table as SmartTable<ActModel>
        view.setData(it)
    }


}
