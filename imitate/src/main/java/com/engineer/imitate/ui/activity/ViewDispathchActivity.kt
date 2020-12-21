package com.engineer.imitate.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.engineer.imitate.R
import com.engineer.imitate.util.toastShort
import kotlinx.android.synthetic.main.activity_view_dispathch.*

class ViewDispathchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_dispathch)

        button.setOnClickListener {
            toastShort("button clicked")
        }


        val fragments = arrayListOf(TransparentFragment(), BlankFragment())
        pager.adapter = MyAdapter(supportFragmentManager, fragments)
    }
}

class TransparentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            return FrameLayout(it)
        }
        return null
    }
}

class BlankFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val child = FrameLayout(it)
            child.setBackgroundColor(Color.RED)
            return child
        }
        return null
    }
}


class MyAdapter(manager: FragmentManager, val fragments: List<Fragment>) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}