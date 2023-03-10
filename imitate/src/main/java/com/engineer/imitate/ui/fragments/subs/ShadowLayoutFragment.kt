package com.engineer.imitate.ui.fragments.subs


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.engineer.imitate.R
import com.engineer.imitate.ui.activity.StarShowActivity


/**
 * A simple [Fragment] subclass.
 */
class ShadowLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shadow_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonPanel: Button = view.findViewById(R.id.buttonPanel)
        buttonPanel.setOnClickListener {
            startActivity(
                Intent(context, StarShowActivity::class.java)
            )
        }
    }
}
