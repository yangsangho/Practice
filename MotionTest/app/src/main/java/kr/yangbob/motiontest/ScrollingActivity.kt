package kr.yangbob.motiontest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlin.math.absoluteValue

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)
        val title = toolbar.javaClass.getDeclaredField("mTitleTextView").let {
            it.isAccessible = true
            it.get(toolbar) as TextView
        }

        var basisScrollRange = 0f
        var doubleBasisScroll = 0f

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (basisScrollRange == 0f) {
                basisScrollRange = appBarLayout.totalScrollRange / 4f
                doubleBasisScroll = basisScrollRange * 2
            }

            val offsetAbs = verticalOffset.absoluteValue

            Log.i("TEST", "offsetAbs = $offsetAbs, basisScrollRange = $basisScrollRange, doubleScroll = $doubleBasisScroll")

            var titleAlpha =
                if (offsetAbs < basisScrollRange) 0f
                else (offsetAbs - basisScrollRange) / doubleBasisScroll
            if (titleAlpha > 1f) titleAlpha = 1f

            val startTitleAlpha =
                if (offsetAbs > doubleBasisScroll) 0f
                else 1f - offsetAbs / doubleBasisScroll

            title.alpha = titleAlpha
            startTitleLayout.alpha = startTitleAlpha

        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }
}
