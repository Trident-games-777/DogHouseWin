package ar.tvplaye.activities

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ar.tvplaye.activities.viewmodels.CatchItemViewModel
import ar.tvplaye.R
import ar.tvplaye.databinding.ActivityDogBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class DogActivity : AppCompatActivity() {
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bndg = ActivityDogBinding.inflate(layoutInflater)
        setContentView(bndg.root)
        val catchItemViewModel = ViewModelProvider(this)[CatchItemViewModel::class.java]
        catchItemViewModel.itemsCount.observe(this) {
            bndg.textView.text = it.toString()
        }

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://ar.tvplaye/raw/smw_coin"))
        mediaPlayer.prepare()


        val pictures =
            listOf(R.drawable.bone1, R.drawable.bone2, R.drawable.bone3, R.drawable.bone4)
        lifecycleScope.launch {
            while (true) {
                imageView = bndg.grid.get(Random.nextInt(bndg.grid.childCount)) as ImageView
                imageView.setImageResource(pictures[Random.nextInt(pictures.size)])
                imageView.setOnClickListener {
                    catchItemViewModel.catch()
                    mediaPlayer.start()
                }
                delay(700)
                imageView.setImageResource(android.R.color.transparent)
                imageView.setOnClickListener(null)
            }
        }
    }
}