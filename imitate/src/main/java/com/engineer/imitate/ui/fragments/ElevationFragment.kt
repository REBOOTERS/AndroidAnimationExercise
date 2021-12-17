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
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.interfaces.SimpleProgressChangeListener
import com.engineer.imitate.util.TestHelper
import com.engineer.imitate.util.toastShort
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.fragment_evelation.*


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/elevation")
class ElevationFragment : Fragment() {

    private var mDeltaX = 0.0f
    private var mDeltaY = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evelation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = ObjectAnimator.ofFloat(fab, "rotation", 180f)
        anim.repeatMode = ObjectAnimator.REVERSE
        anim.repeatCount = ObjectAnimator.INFINITE
        anim.start()
        // start transformation when touching the fab.
        fab.setOnClickListener {
            transformationLayout.startTransform(parent)
        }

        // finish transformation when touching the myCardView.
        myCardView.setOnClickListener {
            transformationLayout.finishTransform(parent)
        }

        cardElevationSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                cardView.cardElevation = progressFloat
                fab.compatElevation = progressFloat
            }
        }

        cardRadiusSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                cardView.radius = progressFloat
            }
        }

        deltaXSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                mDeltaX = progressFloat
                slide_view.update(progressFloat, mDeltaY)
            }
        }

        deltaYSeekBar.onProgressChangedListener = object : SimpleProgressChangeListener() {
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
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
