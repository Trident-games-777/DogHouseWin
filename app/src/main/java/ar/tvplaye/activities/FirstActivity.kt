package ar.tvplaye.activities

import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import ar.tvplaye.NextActivity
import java.io.File

class FirstActivity : AppCompatActivity() {
    val nextActivity = NextActivity()

    val dvlpr: Boolean
        get() = Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED) == "1"


    val rtd: Boolean
        get() =
            try {
                (File("/sbin/su").exists()) ||
                        (File("/system/bin/su").exists()) ||
                        (File("/system/xbin/su").exists()) ||
                        (File("/data/local/xbin/su").exists()) ||
                        (File("/data/local/bin/su").exists()) ||
                        (File("/system/sd/xbin/su").exists()) ||
                        (File("/system/bin/failsafe/su").exists()) ||
                        (File("/data/local/su").exists())
            } catch (e: Exception) {
                false
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(mainLooper).postDelayed({
            if (dvlpr || rtd) {
                nextActivity.next(activity = this, clazz = DogActivity::class.java)
            } else {
                nextActivity.next(activity = this, clazz = MainActivityDog::class.java)
            }
        }, 1500)
    }

}