package ar.tvplaye

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class NextActivity {
    val extra = "extra"

    fun <T> next(activity: Activity, bundle: Bundle? = null, clazz: Class<T>) {
        val i = Intent(activity, clazz)
        bundle?.let {
            i.putExtra(extra, it)
        }
        activity.startActivity(i)
        activity.finish()
    }
}