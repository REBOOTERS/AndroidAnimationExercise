package com.engineer.imitate.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_fresco.*


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/fresco")
class FrescoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fresco, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url= "https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg"

        val player = "http:\\/\\/img.dongqiudi.com\\/data\\/personpic\\/15482.png"

        simpleDraweeView.setImageURI(url)
        Glide.with(this).load(url).into(image)
    }


}
