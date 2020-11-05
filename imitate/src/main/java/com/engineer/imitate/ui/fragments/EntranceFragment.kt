package com.engineer.imitate.ui.fragments


import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.bumptech.glide.Glide
import com.engineer.android.game.ui.GameRootActivity
import com.engineer.imitate.R
import com.engineer.imitate.ui.activity.*
import com.engineer.imitate.ui.activity.fragmentmanager.ContentActivity
import com.engineer.imitate.util.Glide4Engine
import com.engineer.imitate.util.startActivity
import com.engineer.imitate.util.toastShort
import com.permissionx.guolindev.PermissionX
import com.wanglu.photoviewerlibrary.PhotoViewer
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.fragment_entrance.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/entrance")
class EntranceFragment : Fragment() {


    private var datas: MutableList<String> = ArrayList()
    private lateinit var adapter: MyListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entrance, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .request { it, _, _ ->
                    if (it) {
                        Matisse.from(this)
                            .choose(MimeType.ofAll(), false)
                            .countable(true)
                            .capture(true)
                            .captureStrategy(
                                CaptureStrategy(true, requireContext().packageName + ".fileprovider")
                            )
                            .maxSelectable(9)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(Glide4Engine())
                            .forResult(100)
                    }
                }
        }

        set_bg.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .request { it, _, _ ->
                    if (it) {
                        Matisse.from(this)
                            .choose(MimeType.ofAll(), false)
                            .countable(true)
                            .capture(true)
                            .captureStrategy(
                                CaptureStrategy(
                                    true,
                                    requireContext().packageName + ".fileprovider"
                                )
                            )
                            .maxSelectable(9)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(Glide4Engine())
                            .forResult(101)
                    }
                }
        }


        bottom_sheet.setOnClickListener {
            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.comic)
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_content, null)
            bottomsheet.showWithSheetView(view)
        }

        bottom_sheet_google.setOnClickListener {
            val sheet = MyBottomSheetFragment()
            sheet.show(childFragmentManager, EntranceFragment::class.toString())
        }

        inflate_real.setOnClickListener {
            startActivity(Intent(context, InflateRealActivity::class.java))
        }

        final_one.setOnClickListener {
            context?.startActivity<FinalActivity>()
        }


        fake_jike.setOnClickListener {
            // just test weather work-thread start activity is ok
            Thread(Runnable {
                startActivity(
                    Intent(
                        context,
                        FakeJikeActivity::class.java
                    )
                )
            }).start()
        }

        horizontal_list.setOnClickListener {

            //            startActivity(Intent(context, HorizontalListActivity::class.java))

            val bundle = transformationLayout.withView(transformationLayout, "myTransitionName")
            val intent = Intent(context, HorizontalListActivity::class.java)
            intent.putExtra("TransformationParams", transformationLayout.getParcelableParams())
            startActivity(intent, bundle)
        }

        expandable_listview.setOnClickListener {
            context?.startActivity<MyExpandableListViewActivity>()
        }

        date_and_time_picker.setOnClickListener {
            startActivity(Intent(context, DateAndTimePickerActivity::class.java))
        }

        screen_recorder.setOnClickListener {
            startActivity(Intent(context, ScreenRecorderActivity::class.java))
        }

        gif_revert.setOnClickListener {
            startActivity(Intent(context, ReverseGifActivity::class.java))
        }

        game.setOnClickListener {
            startActivity(Intent(context, GameRootActivity::class.java))
        }

        shell.setOnClickListener { startActivity(Intent(context, RunShellActivity::class.java)) }

        constraintLayout_ll.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ConstraintLayoutActivity::class.java
                )
            )
        }
        fragment_manager.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ContentActivity::class.java
                )
            )
        }

        adapter = MyListAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }


    class MyBottomSheetFragment : SuperBottomSheetFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            return inflater.inflate(R.layout.my_bottom_sheet_layout, container, false)
        }

        override fun getStatusBarColor() = Color.RED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    val result = Matisse.obtainPathResult(data)

                    if (result != null) {
                        datas.clear()
                        datas.addAll(result)
                        adapter.notifyDataSetChanged()
                    }

//                    val shareIntent = ShareCompat.IntentBuilder.from(activity)
//                        .addStream(Matisse.obtainResult(data)[0])
////                        .addStream(revertedlUrl)
//                        .setText("https:www.zhihu.com")
//                        .setType("text/plain")
//                        .createChooserIntent()
//                        .addFlags(
//                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT
//                                    or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
//                                    or Intent.FLAG_ACTIVITY_NEW_TASK
//                        )
//                    startActivity(shareIntent)

                }
                101 -> {
                    val result = Matisse.obtainPathResult(data)
                    val bitmap = BitmapFactory.decodeFile(File(result[0]).absolutePath)
                    val wallpaperManager = WallpaperManager.getInstance(context)

                    try {
                        wallpaperManager.setBitmap(bitmap)
                        context?.toastShort("set wall paper success")

                    } catch (e: Exception) {
                        context?.toastShort("set wall paper fail")

                    }
                }

            }

        }
    }


    private inner class MyListAdapter : RecyclerView.Adapter<MyListAdapter.Holder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            context = parent.context
//            var view =
//                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            val holder = Holder(imageView)
            imageView.setOnClickListener {
                val pos = holder.adapterPosition
                val url = datas[pos]
                PhotoViewer.setClickSingleImg(url, it)
                    .setShowImageViewInterface(object : PhotoViewer.ShowImageViewInterface {
                        override fun show(iv: ImageView, url: String) {
                            Glide.with(this@EntranceFragment).load(url).into(iv)
                        }
                    }).start(this@EntranceFragment)
            }
            return holder
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            Glide.with(context).load(datas[position]).into(holder.image)
        }

        private lateinit var context: Context

        private inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
            var image: ImageView = view as ImageView
        }
    }


}
