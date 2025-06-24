package com.engineer.imitate.ui.fragments


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.interfaces.SimpleProgressChangeListener
import com.engineer.imitate.ui.widget.custom.JikeSlideView
import com.engineer.imitate.util.TestHelper
import com.engineer.imitate.util.toastShort
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/elevation")
class ElevationFragment : Fragment() {

    private var mDeltaX = 0.0f
    private var mDeltaY = 0.0f

    private lateinit var fab: FloatingActionButton

    private lateinit var parent: ConstraintLayout
    private lateinit var myCardView: CardView
    private lateinit var cardView: CardView
    private lateinit var cardElevationSeekBar: AppCompatSeekBar
    private lateinit var cardRadiusSeekBar: AppCompatSeekBar
    private lateinit var deltaXSeekBar: AppCompatSeekBar
    private lateinit var deltaYSeekBar: AppCompatSeekBar
    private lateinit var slide_view: JikeSlideView
    private lateinit var textView: TextView
    private lateinit var notification: TextView
    private lateinit var open_push_setting: Button
    private lateinit var open_system_share: Button
    private lateinit var read_sp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evelation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = view.findViewById(R.id.fab)
        parent = view.findViewById(R.id.parent)

        myCardView = view.findViewById(R.id.myCardView)
        cardView = view.findViewById(R.id.cardView)
        cardElevationSeekBar = view.findViewById(R.id.cardElevationSeekBar)
        cardRadiusSeekBar = view.findViewById(R.id.cardRadiusSeekBar)
        deltaXSeekBar = view.findViewById(R.id.deltaXSeekBar)
        deltaYSeekBar = view.findViewById(R.id.deltaYSeekBar)
        deltaYSeekBar = view.findViewById(R.id.deltaYSeekBar)
        slide_view = view.findViewById(R.id.slide_view)
        textView = view.findViewById(R.id.textView)
        notification = view.findViewById(R.id.notification)
        open_push_setting = view.findViewById(R.id.open_push_setting)
        open_system_share = view.findViewById(R.id.open_system_share)
        read_sp = view.findViewById(R.id.read_sp)

        val anim = ObjectAnimator.ofFloat(fab, "rotation", 180f)
        anim.repeatMode = ObjectAnimator.REVERSE
        anim.repeatCount = ObjectAnimator.INFINITE
        anim.start()
        // start transformation when touching the fab.

        cardElevationSeekBar.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                super.onProgressChanged(p0, p1, p2)
                cardView.cardElevation = p1.toFloat()
                fab.compatElevation = p1.toFloat()
            }
        })

        cardRadiusSeekBar.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                super.onProgressChanged(p0, p1, p2)
                cardView.radius = p1.toFloat()
            }
        })
        deltaXSeekBar.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                super.onProgressChanged(p0, p1, p2)
                mDeltaX = p1.toFloat()
                slide_view.update(p1.toFloat(), mDeltaY)
            }

        })
        deltaYSeekBar.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                super.onProgressChanged(p0, p1, p2)

                val screenHeight = resources.displayMetrics.heightPixels
                val delatY = p1.toFloat() / deltaYSeekBar.max * screenHeight
                mDeltaY = delatY
                slide_view.update(mDeltaX, delatY)
            }
        })


        textView.setOnClickListener {
            context?.toastShort("context extension !")
        }


        open_push_setting.setOnClickListener {
            if (context != null) {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                }
                intent.data = Uri.fromParts("package", context?.packageName, null)

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    tryThis()
                    e.printStackTrace()
                }
            }
        }

        open_system_share.setOnClickListener {
            //            val shareIntent = ShareCompat.IntentBuilder.from(activity)
//                    .setText("share content")
//                    .setType("text/plain")
//                    .createChooserIntent()
//                    .apply {
//                        // https://android-developers.googleblog.com/2012/02/share-with-intents.html
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            // If we're on Lollipop, we can open the intent as a document
//                            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//                        } else {
//                            // Else, we will use the old CLEAR_WHEN_TASK_RESET flag
//                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
//                        }
//                    }
//            startActivity(shareIntent)
        }

        read_sp.setOnClickListener {
            val sp = it.context.getSharedPreferences("shared_prefs_doraemon", Context.MODE_PRIVATE)
            val map = sp.all
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                map.forEach { t, any ->
                    Log.e("read_sp", "key = $t, value = $any")
                }
            }
        }


        view.findViewById<Button>(R.id.start11).setOnClickListener {
            TestHelper.openActivity(it.context)
        }
        view.findViewById<Button>(R.id.start22).setOnClickListener {
            TestHelper.openActivityHome(it.context)
        }
        view.findViewById<Button>(R.id.start33).setOnClickListener {
            TestHelper.openActivityByUrl(it.context)
        }

    }

    private fun tryThisOne() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"

        //for Android 5-7
        intent.putExtra("app_package", context?.packageName)
        intent.putExtra("app_uid", context?.applicationInfo?.uid)

        // for Android O
        intent.putExtra("android.provider.extra.APP_PACKAGE", context?.packageName)


        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun tryThis() {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 -> {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", context?.getPackageName())
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context?.getPackageName())
                intent.putExtra("app_uid", context?.getApplicationInfo()?.uid)
            }

            else -> {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + context?.packageName)
            }
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            tryThisOne()
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            notification.text =
                NotificationManagerCompat.from(it).areNotificationsEnabled().toString()
        }


    }


}
