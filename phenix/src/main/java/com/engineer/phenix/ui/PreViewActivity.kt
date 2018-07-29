package com.engineer.phenix.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.engineer.phenix.Phenix
import com.engineer.phenix.R
import kotlinx.android.synthetic.main.activity_pre_view.*

class PreViewActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_view)


        viewPager.adapter = ViewPagerAdapter(Phenix.getInstance().getDatas())
    }


}
