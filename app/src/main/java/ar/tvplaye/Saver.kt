package ar.tvplaye

import android.content.Context

class Saver(val context: Context) {
    private val defaultValue = "https://doghousewin.xyz"
    private val filename = "myFile.txt"

    fun saveOrTake(take: Boolean = false, value: String = defaultValue): String {
        if (take) {
            //Taking
            context.openFileInput(filename).bufferedReader().useLines { lines ->
                return lines.ifEmpty { sequenceOf("") }.first()
            }
        } else {
            //Saving
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(value.toByteArray())
            }
            return ""
        }
    }
}