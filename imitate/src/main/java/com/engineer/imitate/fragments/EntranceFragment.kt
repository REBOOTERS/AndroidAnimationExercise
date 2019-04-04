package com.engineer.imitate.fragments


import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.engineer.imitate.R
import com.engineer.imitate.activity.CustomScrollingActivity
import com.engineer.imitate.activity.FakeJikeActivity
import com.engineer.imitate.activity.FinalActivity
import com.engineer.imitate.activity.PureActivity
import com.engineer.imitate.util.Glide4Engine
import com.engineer.imitate.util.toastShort
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        custom_scrolling.setOnClickListener {
            startActivity(Intent(context, CustomScrollingActivity::class.java))
        }

        bottom_sheet.setOnClickListener {
            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.comic)
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_content, null)
            bottomsheet.showWithSheetView(view)
        }

        pure.setOnClickListener {
            startActivity(Intent(context, PureActivity::class.java))
        }

        final_one.setOnClickListener {
            startActivity(Intent(context, FinalActivity::class.java))
        }
        fake_jike.setOnClickListener {
            startActivity(Intent(context, FakeJikeActivity::class.java))
        }



        adapter = MyListAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
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
