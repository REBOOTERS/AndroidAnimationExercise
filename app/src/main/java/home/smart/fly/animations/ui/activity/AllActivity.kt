package home.smart.fly.animations.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zhihu.android.sugaradapter.SugarAdapter
import home.smart.fly.animations.R
import home.smart.fly.animations.sugar.viewholder.ActivityListViewHolder
import home.smart.fly.animations.utils.AppUtils
import kotlinx.android.synthetic.main.activity_all.*
import kotlinx.android.synthetic.main.content_all.*
import java.util.*

class AllActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val adapter = SugarAdapter.Builder.with(getActivities())
                .add(ActivityListViewHolder::class.java)
                .build();
        adapter.addSugarHolderListener(object : SugarAdapter.SugarHolderListener<ActivityListViewHolder>() {
            override fun onSugarHolderBindData(holder: ActivityListViewHolder) {
                super.onSugarHolderBindData(holder)
                holder.itemshell.setOnClickListener {
                    val data = holder.data
                    val target = Class.forName(data.second)
                    val intent = Intent(this@AllActivity, target)
                    startActivity(intent)
                }
            }
        })
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        title = getString(R.string.activity_count, adapter.list.size)
    }

    private fun getActivities(): List<Pair<String, String>> {
        val results = ArrayList<Pair<String, String>>()
        val info = packageManager
                .getPackageInfo(AppUtils.getPackageName(this), PackageManager.GET_ACTIVITIES)
        for (item in info.activities) {
            val fullName = item.name
            val simpleName = fullName.substring(fullName.lastIndexOf(".") + 1)
            val pair = Pair(simpleName, fullName)
            if (simpleName.equals("AllActivity")) {
                continue
            }
            results.add(pair)
        }
        Collections.sort(results, object : Comparator<Pair<String, String>> {
            override fun compare(o1: Pair<String, String>?, o2: Pair<String, String>?): Int {
                return o1!!.first.compareTo(o2!!.first)
            }
        })

        return results
    }
}
