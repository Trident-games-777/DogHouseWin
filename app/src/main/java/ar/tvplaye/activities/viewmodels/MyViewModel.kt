package ar.tvplaye.activities.viewmodels

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import java.util.*
import kotlin.concurrent.thread

class MyViewModel : ViewModel() {
    var gadidObserver: GadidObserver? = null
    var isViewModelLaunched = false
    val urlLiveData = MutableLiveData<String>()
    val gadidLiveData = MutableLiveData<String>()

    fun launchViewModel(context: Context) {
        isViewModelLaunched = true
        gadidObserver = GadidObserver(context)

        gadidLiveData.observeForever(gadidObserver!!)
        thread {
            gadidLiveData.postValue(AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        gadidObserver?.let {
            gadidLiveData.removeObserver(it)
        }
    }

    inner class GadidObserver(val context: Context) : Observer<String> {
        override fun onChanged(t: String?) {
            t?.let { ad ->
                OneSignal.initWithContext(context)
                OneSignal.setAppId("fca38b63-0bf2-40f6-b985-ea66b405941e")
                OneSignal.setExternalUserId(ad)

                AppLinkData.fetchDeferredAppLinkData(context) { fb ->
                    val fbLink = fb?.targetUri.toString()
                    if (fbLink != "null") {
                        urlLiveData.postValue(createUrl(fbLink, ad))
                        OneSignal.sendTag(
                            "key2",
                            fbLink.replace("myapp://", "").substringBefore("/")
                        )
                    } else {
                        AppsFlyerLib.getInstance()
                            .init("9sRnTAmKxBV9icHJmKEimj", AppsFlyerObserver(ad, context), context)
                        AppsFlyerLib.getInstance().start(context)
                    }
                }
            }
        }
    }

    inner class AppsFlyerObserver(val gadid: String, val context: Context) :
        AppsFlyerConversionListener {
        override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
            urlLiveData.postValue(
                createUrl(
                    p0, gadid, AppsFlyerLib.getInstance()
                        .getAppsFlyerUID(context)
                )
            )
            if (p0?.get("campaign").toString() != "null") {
                OneSignal.sendTag(
                    "key2",
                    p0?.get("campaign").toString().substringBefore("_")
                )
            } else {
                OneSignal.sendTag("key2", "organic")
            }
        }

        override fun onConversionDataFail(p0: String?) {
            urlLiveData.postValue(createUrl(gadid))
            OneSignal.sendTag("key2", "organic")
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

        }

        override fun onAttributionFailure(p0: String?) {

        }

    }

    fun createUrl(map: MutableMap<String, Any>?, gadid: String, uid: String): String {
        return "https://doghousewin.xyz/dhwin.php".toUri().buildUpon().apply {
            appendQueryParameter("K63aE4jbC7", "wkbVOQAfBO")
            appendQueryParameter("qKWbDZD4vZ", TimeZone.getDefault().id)
            appendQueryParameter("9bRSA0xwaT", gadid)
            appendQueryParameter("rAJUMgGvnn", "null")
            appendQueryParameter("hwGqHoLx7l", map?.get("media_source").toString())
            appendQueryParameter("9yV1t3qhfe", uid)
            appendQueryParameter("geibpe38JG", map?.get("adset_id").toString())
            appendQueryParameter("AuoR7vtJxX", map?.get("campaign_id").toString())
            appendQueryParameter("AkvdgU3zgQ", map?.get("campaign").toString())
            appendQueryParameter("GM2uaYEdQF", map?.get("adset").toString())
            appendQueryParameter("QYMCK5UvWB", map?.get("adgroup").toString())
            appendQueryParameter("rT6kFFEOoa", map?.get("orig_cost").toString())
            appendQueryParameter("jbeQc5Lnj5", map?.get("af_siteid").toString())
        }.toString()
    }

    fun createUrl(gadid: String): String {
        return "https://doghousewin.xyz/dhwin.php".toUri().buildUpon().apply {
            appendQueryParameter("K63aE4jbC7", "wkbVOQAfBO")
            appendQueryParameter("qKWbDZD4vZ", TimeZone.getDefault().id)
            appendQueryParameter("9bRSA0xwaT", gadid)
            appendQueryParameter("rAJUMgGvnn", "null")
            appendQueryParameter("hwGqHoLx7l", "null")
            appendQueryParameter("9yV1t3qhfe", "null")
            appendQueryParameter("geibpe38JG", "null")
            appendQueryParameter("AuoR7vtJxX", "null")
            appendQueryParameter("AkvdgU3zgQ", "null")
            appendQueryParameter("GM2uaYEdQF", "null")
            appendQueryParameter("QYMCK5UvWB", "null")
            appendQueryParameter("rT6kFFEOoa", "null")
            appendQueryParameter("jbeQc5Lnj5", "null")
        }.toString()
    }

    fun createUrl(fbLink: String, gadid: String): String {
        return "https://doghousewin.xyz/dhwin.php".toUri().buildUpon().apply {
            appendQueryParameter("K63aE4jbC7", "wkbVOQAfBO")
            appendQueryParameter("qKWbDZD4vZ", TimeZone.getDefault().id)
            appendQueryParameter("9bRSA0xwaT", gadid)
            appendQueryParameter("rAJUMgGvnn", fbLink)
            appendQueryParameter("hwGqHoLx7l", "deeplink")
            appendQueryParameter("9yV1t3qhfe", "null")
            appendQueryParameter("geibpe38JG", "null")
            appendQueryParameter("AuoR7vtJxX", "null")
            appendQueryParameter("AkvdgU3zgQ", "null")
            appendQueryParameter("GM2uaYEdQF", "null")
            appendQueryParameter("QYMCK5UvWB", "null")
            appendQueryParameter("rT6kFFEOoa", "null")
            appendQueryParameter("jbeQc5Lnj5", "null")
        }.toString()
    }

}