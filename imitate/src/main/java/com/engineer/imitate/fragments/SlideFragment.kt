package com.engineer.imitate.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.adapter.SlideListAdapter
import com.engineer.imitate.widget.view.layoutmanager.ItemConfig
import com.engineer.imitate.widget.view.layoutmanager.ItemTouchHelperCallback
import com.engineer.imitate.widget.view.layoutmanager.OnSlideListener
import com.engineer.imitate.widget.view.layoutmanager.SlideLayoutManager
import kotlinx.android.synthetic.main.fragment_slide.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/slide")
class SlideFragment : androidx.fragment.app.Fragment() {

    private var datas: MutableList<String> = ArrayList()
    private lateinit var mItemTouchHelperCallback: ItemTouchHelperCallback<String>
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private var mOnSlideListener: SimpleOnSlideListener = SimpleOnSlideListener()


    var array1: Array<String> = arrayOf("http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1ffla9ostxuj20ku0q2abt.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-12-18380140_455327614813449_854681840315793408_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-11-18380166_305443499890139_8426655762360565760_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-04-18299181_1306649979420798_1108869403736276992_n.jpg",
            "http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg")


    var array2: Array<String> = arrayOf(
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-27-17934080_117414798808566_8957027985114791936_n.jpg?imageslim",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-25-13651793_897557617014845_571817176_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-24-18013547_1532023163498554_215541963087151104_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-24-18094714_158946097967074_5909424912493182976_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-21-18013964_1389732981073150_4044061109068496896_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-20-17932350_1238028809599424_2089669844847820800_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-19-17881407_1845958195665029_1132383288824954880_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-18-17882540_190116561497334_440657494176432128_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-16-17934400_1738549946443321_2924146161843437568_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-14-17881962_1329090457138411_8289893708619317248_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-13-17882785_926451654163513_7725522121023029248_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-12-17662441_1675934806042139_7236493360834281472_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-11-17881546_248332202297978_2420944671002853376_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-09-17586558_426275167734768_6312107349515436032_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-07-17817932_274034076387428_5240190736292380672_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-06-17493825_1061197430652762_1457834104966873088_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-04-17438270_1418311001574160_8728796670000627712_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-31-17662615_1819763634937161_2829588299293655040_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-30-17265582_1877445642507654_3057988544061505536_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-29-17267498_392021674501739_8632065627013513216_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-28-17494350_115579865647756_2448160714821468160_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-26-17495078_643307445877569_4485136026821459968_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-24-17438359_1470934682925012_1066984844010979328_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-23-17265820_645330569008169_4543676027339014144_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-22-17332868_1929305090624552_8918542166154805248_n.jpg",
            "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-21-17268102_763630507137257_3620762734536163328_n%20-1-.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-20-17333300_1680707251945881_2009298023053524992_n.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-17-17332809_1277469728986540_3201752429582352384_n.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-16-17333221_108837802984751_2789267558635667456_n.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-15-17126482_115753765623699_4225645012014071808_n.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-13-17267506_264626920661300_5781854075880472576_n.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-13-17265708_396005157434387_3099040288153272320_n.jpg")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        list.adapter = SlideListAdapter(datas)
        mItemTouchHelperCallback = ItemTouchHelperCallback(list.adapter as SlideListAdapter, datas)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mSlideLayoutManager = SlideLayoutManager(list, mItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(list)
        list.layoutManager = mSlideLayoutManager
        mItemTouchHelperCallback.setOnSlideListener(mOnSlideListener)
    }


    var index: Int = 0

    private inner class SimpleOnSlideListener : OnSlideListener<String> {
        override fun onSliding(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, ratio: Float, direction: Int) {
        }

        override fun onSlided(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, t: String, direction: Int) {
            datas.add(datas.size - 1, array2[index])
            index += 1

            when (direction) {
                ItemConfig.SLIDED_LEFT -> Toast.makeText(context, "赞", Toast.LENGTH_SHORT).show()
                ItemConfig.SLIDED_RIGHT -> Toast.makeText(context, "讨厌", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClear() {
        }

    }


    private fun initData() {


        datas.addAll(array1)
    }


}
