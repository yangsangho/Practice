package kr.yangbob.unitanduitest

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE, sdk = [Build.VERSION_CODES.Q])
class RobolectricTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun addition_isCorrect() {
        assertThat(context.packageName, `is`("kr.yangbob.unitanduitest"))
        assertThat(context.getString(R.string.app_name), `is`("UnitAndUiTest"))

        // Locale 설정 하는 법 찾아야함
        println(Locale.getDefault())
    }
}