package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.StatusBarUtil

class MergeIncludeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColor(this,ContextCompat.getColor(this,R.color.colorPrimary))
        setContentView(R.layout.activity_merge_include_view)
        findViewById<View>(R.id.back).setOnClickListener { finish() }
    }
}
