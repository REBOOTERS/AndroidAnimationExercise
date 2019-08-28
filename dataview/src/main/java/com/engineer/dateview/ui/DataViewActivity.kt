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
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_data_view.*


internal class DataViewActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        disposable = DataView.getRepository()
            .all
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (disposable != null && disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }
}
