package com.engineer.imitate

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.engineer.imitate.util.AppUtils
import com.facebook.soloader.SoLoader
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * @author rookie
 * @since 07-30-2019
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27],application = ImitateApplication::class)
class AndroidContextTest {

    @Test
    fun assertContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val version = AppUtils.getAppVersion(context)
        // 单元测试，也可以打印日志
        println("version ==$version")

        assertEquals("1.0", version)
        assertEquals("com.engineer.imitate",AppUtils.getAppName(context))
    }

    companion object {

        @BeforeClass
        fun setup() {
            // for Fresco
            SoLoader.setInTestMode()
        }
    }
}
