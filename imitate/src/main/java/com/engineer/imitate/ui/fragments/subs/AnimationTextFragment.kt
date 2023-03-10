package com.engineer.imitate.ui.fragments.subs


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import androidx.fragment.app.Fragment
import com.engineer.imitate.R
import com.engineer.imitate.ui.widget.AnimatedCountTextView
import com.engineer.imitate.ui.widget.CountEndListener

/**
 * A simple [Fragment] subclass.
 */
class AnimationTextFragment : Fragment(), CountEndListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playButton: Button = view.findViewById(R.id.playButton)
        val countTextView: AnimatedCountTextView = view.findViewById(R.id.countTextView)
        playButton.setOnClickListener {
            countTextView.apply {
                countEndListener(this@AnimationTextFragment)
                interpolator(AccelerateInterpolator())
                start()
            }
        }
    }

    override fun onCountFinish() {
        Log.d("MainActivity", "onCountFinish")
    }
}
