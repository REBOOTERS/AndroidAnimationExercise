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
import com.engineer.imitate.R
import com.engineer.imitate.ui.activity.*
import com.engineer.imitate.util.Glide4Engine
import com.engineer.imitate.util.toastShort
import com.tbruyelle.rxpermissions2.RxPermissions
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entrance, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            val permissions = RxPermissions(this)
            permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe {
                        Matisse.from(this)
                                .choose(MimeType.ofAll(), false)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        CaptureStrategy(true, context!!.packageName + ".fileprovider"))
                                .maxSelectable(9)
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .thumbnailScale(0.85f)
                                .imageEngine(Glide4Engine())
                                .forResult(100)
                    }
        }

        set_bg.setOnClickListener {
            val permissions = RxPermissions(this)
            permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe {
                        Matisse.from(this)
                                .choose(MimeType.ofAll(), false)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(
                                        CaptureStrategy(true, context!!.packageName + ".fileprovider"))
                                .maxSelectable(9)
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .thumbnailScale(0.85f)
                                .imageEngine(Glide4Engine())
                                .forResult(101)
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



        final_one.setOnClickListener {
            startActivity(Intent(context, FinalActivity::class.java))
        }


        fake_jike.setOnClickListener {
            // just test weather work-thread start activity is ok
            Thread(Runnable { startActivity(Intent(context, FakeJikeActivity::class.java)) }).start()
        }

        expandable_listview.setOnClickListener {
            startActivity(Intent(context, MyExpandableListViewActivity::class.java))
        }

        date_and_time_picker.setOnClickListener {
            startActivity(Intent(context, DateAndTimePickerActivity::class.java))
        }

        screen_recorder.setOnClickListener {
            startActivity(Intent(context, ScreenRecorderActivity::class.java))
        }


        adapter = MyListAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }

    class MyBottomSheetFragment : SuperBottomSheetFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            var view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            Glide.with(context).load(datas[position]).into(holder.image)
        }

        private lateinit var context: Context

        private inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
            var image: ImageView = view.findViewById(R.id.image);
        }
    }


}
