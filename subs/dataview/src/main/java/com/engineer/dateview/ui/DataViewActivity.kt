package com.engineer.dateview.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bin.david.form.core.SmartTable
import com.engineer.dateview.R
import com.engineer.dateview.api.DataView
import com.engineer.dateview.internal.AutoSchedulerTransformer
import com.engineer.dateview.model.ActModel
import io.reactivex.disposables.Disposable


internal class DataViewActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private lateinit var table: SmartTable<ActModel>

    @SuppressLint("CheckResult")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)
        table = findViewById<SmartTable<ActModel>>(R.id.table)

        disposable = DataView.getRepository().all.compose(AutoSchedulerTransformer()).subscribe({
            Log.e("ui", "size==${it.size}")

            loadUI(it)

        }, {
            it.printStackTrace()
        })
    }

    private fun loadUI(it: List<ActModel>) {
        table.setData(it)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (disposable != null && disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }
}
