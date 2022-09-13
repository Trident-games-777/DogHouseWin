package ar.tvplaye

import android.app.Application
import android.content.Context
import java.io.File

class DogsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val filename = "myFile.txt"
        if (!File(filesDir, filename).exists()) {
            openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write("".toByteArray())
            }
        }
    }
}