package com.engineer.imitate.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.widget.custom.CustomScrollView
import com.engineer.imitate.util.toastShort

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/scroller")
class ScrollerFragment : Fragment() {
    private var toggle: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customView: CustomScrollView = view.findViewById(R.id.customView)
        customView.setOnClickListener {
            context?.toastShort("click")
            if (toggle) {
                customView.smoothScrollTo(195, 195)
            } else {
                customView.smoothScrollTo(0, 0)
            }
            toggle = !toggle
        }
    }


}
