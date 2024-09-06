package home.smart.fly.animations.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.AppUtils
import home.smart.fly.animations.utils.bind
import java.util.*

class AllActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
       val list = getActivities()
        title = getString(R.string.activity_count, list.size)
        recyclerView.bind(list, R.layout.demo_info_item) { data, i ->
            val titleTv:TextView = findViewById(R.id.title)
            val descTv:TextView = findViewById(R.id.desc)
            titleTv.text= data.first
            descTv.text = data.second
            this.setOnClickListener {
                val target = Class.forName(data.second)
                startActivity(Intent(this@AllActivity, target))
            }
        }

    }

    private fun getActivities(): List<Pair<String, String>> {
        val results = ArrayList<Pair<String, String>>()
        val info = packageManager.getPackageInfo(AppUtils.getPackageName(this), PackageManager.GET_ACTIVITIES)
        info.activities?.let {
            for (item in it) {
                val fullName = item.name
                val simpleName = fullName.substring(fullName.lastIndexOf(".") + 1)
                val pair = Pair(simpleName, fullName)
                if (simpleName == "AllActivity") {
                    continue
                }
                results.add(pair)
            }
        }

        results.sortWith { o1, o2 -> o1!!.first.compareTo(o2!!.first) }

        return results
    }
}
