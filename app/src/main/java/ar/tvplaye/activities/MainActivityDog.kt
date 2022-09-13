package ar.tvplaye.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import ar.tvplaye.NextActivity
import ar.tvplaye.R
import ar.tvplaye.Saver
import ar.tvplaye.activities.viewmodels.MyViewModel

class MainActivityDog : AppCompatActivity() {
    lateinit var myViewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dog)
        val saver = Saver(this)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        val nextActivity = NextActivity()

        if (saver.saveOrTake(take = true).isNotEmpty()) {
            val url = saver.saveOrTake(take = true)
            nextActivity.next(
                activity = this,
                bundle = bundleOf("url" to url),
                clazz = DogWebViewActivity::class.java
            )
        } else {
            if (!myViewModel.isViewModelLaunched) {
                myViewModel.launchViewModel(this)
            }

            myViewModel.urlLiveData.observe(this) { url ->
                nextActivity.next(
                    activity = this,
                    bundle = bundleOf("url" to url),
                    clazz = DogWebViewActivity::class.java
                )
            }
        }
    }
}