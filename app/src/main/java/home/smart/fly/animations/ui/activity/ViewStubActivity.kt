package home.smart.fly.animations.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_view_stub.*

class ViewStubActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_stub)
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimary), 0);
        load.setOnClickListener {
            var viewStub = stub.inflate()
            Toast.makeText(this, "ViewStub.inflate() consume only once !",Toast.LENGTH_SHORT).show()
            load.isEnabled = false
        }
    }
}
