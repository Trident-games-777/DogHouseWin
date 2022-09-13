package ar.tvplaye.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatchItemViewModel : ViewModel() {
    val itemsCount = MutableLiveData(0)
    fun catch() {
        val newValue = itemsCount.value!! + 1
        itemsCount.postValue(newValue)
    }
}