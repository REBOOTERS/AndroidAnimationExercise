package com.engineer.imitate.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationManagerCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.interfaces.SimpleProgressChangeListener
import com.engineer.imitate.util.toastShort
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.fragment_evelation.*
import android.support.v4.content.ContextCompat.startActivity
import android.os.Build


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/elevation")
class ElevationFragment : Fragment() {

    private var mDeltaX = 0.0f
    private var mDeltaY = 0.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evelation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardElevationSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                cardView.cardElevation = progressFloat
                fab.compatElevation = progressFloat
            }
        }

        cardRadiusSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                cardView.radius = progressFloat
            }
        }

        deltaXSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                mDeltaX = progressFloat
                slide_view.update(progressFloat, mDeltaY)
            }
        }

        deltaYSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                val screenHeight = resources.displayMetrics.heightPixels
                val delatY = progressFloat / deltaYSeekBar.max * screenHeight
                mDeltaY = delatY
                slide_view.update(mDeltaX, delatY)
            }
        }


        textView.setOnClickListener {
            context?.toastShort("context extension !")
        }


        open_push_setting.setOnClickListener {
            if (context != null) {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                intent.data = Uri.fromParts("package", context!!.getPackageName(), null)

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

//                val intent = Intent()
//                when {
//                    android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 -> {
//                        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
//                        intent.putExtra("android.provider.extra.APP_PACKAGE", context!!.getPackageName())
//                    }
//                    android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
//                        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
//                        intent.putExtra("app_package", context!!.getPackageName())
//                        intent.putExtra("app_uid", context!!.getApplicationInfo().uid)
//                    }
//                    else -> {
//                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        intent.addCategory(Intent.CATEGORY_DEFAULT)
//                        intent.data = Uri.parse("package:" + context!!.getPackageName())
//                    }
//                }
//                val intent = Intent()
//                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
//
////for Android 5-7
////                intent.putExtra("app_package", context!!.getPackageName())
////                intent.putExtra("app_uid", context!!.getApplicationInfo().uid)
//
//// for Android O
//                intent.putExtra("android.provider.extra.APP_PACKAGE", context!!.getPackageName())
//
//
//                startActivity(intent)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        notification.text = NotificationManagerCompat.from(this.context!!).areNotificationsEnabled().toString()

    }


}
