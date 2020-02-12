package kr.yangbob.motiontest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goBasic.setOnClickListener {
            startActivity(Intent(this, BasicActivity::class.java))
        }
        goKeyFrame.setOnClickListener {
            startActivity(Intent(this, KeyFrameActivity::class.java))
        }
        goMyTest.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity::class.java))
        }
    }
}
