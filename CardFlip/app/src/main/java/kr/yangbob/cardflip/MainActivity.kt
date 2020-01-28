package kr.yangbob.cardflip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    var isShowingBackLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.flip_layout, FlipFront())
                .commit()
        } else {
            isShowingBackLayout = supportFragmentManager.backStackEntryCount > 0
        }

        supportFragmentManager.addOnBackStackChangedListener(this)
        flip_layout.setOnClickListener {
            flipCard()
        }
        val scale = resources.displayMetrics.density
        flip_layout.cameraDistance = 8000 * scale
    }

    override fun onBackStackChanged() {
        isShowingBackLayout = supportFragmentManager.backStackEntryCount > 0
    }

    private fun flipCard() {
        if (isShowingBackLayout) {
            supportFragmentManager.popBackStack()
            return
        }
        isShowingBackLayout = true
        supportFragmentManager.beginTransaction()
            //커스텀 애니메이션
            .setCustomAnimations(
                R.animator.cardflip_right_in, R.animator.cardflip_right_out,
                R.animator.cardflip_left_in, R.animator.cardflip_left_out)
            // 뒷면으로 바뀜
            .replace(R.id.flip_layout, FlipBack())
            // 뒤로가기 누르면 앞면을 보여줌
            .addToBackStack(null)
            .commit()
    }
}

class FlipFront : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.flip_front, container, false)
    }
}

class FlipBack : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.flip_back, container, false)
    }
}