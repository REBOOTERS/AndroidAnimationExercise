package com.engineer.imitate.ui.fragments.di


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.ImitateApplication
import com.engineer.imitate.R
import javax.inject.Inject

/**
 * https://developer.android.google.cn/training/dependency-injection/dagger-basics?hl=zh-cn#kotlin
 *
 * Dagger 入门
 */
@Route(path = "/anim/dependency_injection")
class DIFragment : Fragment() {
    private val TAG = "DIFragment"

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_di, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testDagger()

        loginViewModel.request {
            Log.e(TAG, "onViewCreated: result = $it")
        }
    }

    private fun testDagger() {


//        val applicationGraph = DaggerApplicationGraph.create()
//
//        val userRepo1 = applicationGraph.repository()
//        val userRepo2 = applicationGraph.repository()
//        Log.e(TAG, "testDagger: user1 == user2 ${userRepo1 == userRepo2}")


        val myGraph = DaggerMyGraph.create()
        val obj1 = myGraph.getObj()
        val obj2 = myGraph.getObj()
        Log.e(TAG, "testDagger: obj1 == obj2 ${obj1 == obj2}")
        Log.e(TAG, "testDagger: obj.getInfo() = ${obj1.getInfo()}")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val app = getContext()?.applicationContext as ImitateApplication
        app.applicationComponent?.inject(this)

    }

}
