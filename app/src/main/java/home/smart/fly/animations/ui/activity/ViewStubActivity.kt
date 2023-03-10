package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.ViewStub
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.StatusBarUtil

const val tag = "viewstub"

class ViewStubActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_stub)
        val load: Button = findViewById(R.id.load)
        val stub: ViewStub = findViewById(R.id.stub)
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimary), 0)
        Log.e(tag, "clickable==" + load.isClickable)
        load.setOnClickListener {
            var viewStub = stub.inflate()
            Toast.makeText(this, "ViewStub.inflate() consume only once !", Toast.LENGTH_SHORT).show()
            load.isEnabled = false
        }
        Log.e(tag, "now clickable==" + load.isClickable)
    }
}
