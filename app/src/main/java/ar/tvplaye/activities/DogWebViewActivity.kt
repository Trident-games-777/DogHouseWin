package ar.tvplaye.activities

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import ar.tvplaye.NextActivity
import ar.tvplaye.R
import ar.tvplaye.Saver
import ar.tvplaye.databinding.ActivityDogWebViewBinding
import kotlinx.coroutines.launch

class DogWebViewActivity : AppCompatActivity() {
    lateinit var bndg: ActivityDogWebViewBinding
    lateinit var nextActivity: NextActivity
    lateinit var saver: Saver
    lateinit var chooserCallback: ValueCallback<Array<Uri?>>

    val urlForChecking = "https://doghousewin.xyz/"
    val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        chooserCallback.onReceiveValue(it.toTypedArray())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bndg = ActivityDogWebViewBinding.inflate(layoutInflater)
        setContentView(bndg.root)
        setBlackStatusBar()
        setOnBackClicked(bndg.myWebView)
        nextActivity = NextActivity()
        saver = Saver(this)

        val urlBundle = intent.getBundleExtra(nextActivity.extra)!!
        val url = urlBundle.getString("url")!!
        bndg.myWebView.loadUrl(url)

        bndg.myWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
                if (url == urlForChecking) {
                    nextActivity.next(
                        activity = this@DogWebViewActivity,
                        clazz = DogActivity::class.java
                    )
                } else {
                    if (!url.startsWith(urlForChecking) && saver.saveOrTake(take = true)
                            .isEmpty()
                    ) {
                        saver.saveOrTake(value = url)
                    }
                }
            }
        }

        with(bndg.myWebView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = false
            userAgentString = System.getProperty("http.agent")
        }

        with(CookieManager.getInstance()) {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(bndg.myWebView, true)
        }

        bndg.myWebView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri?>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                chooserCallback = filePathCallback
                getContent.launch("image/*")
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val createdWebView = WebView(this@DogWebViewActivity)
                createdWebView.settings.javaScriptEnabled = true
                createdWebView.webChromeClient = this
                createdWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                createdWebView.settings.domStorageEnabled = true
                createdWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = createdWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    fun setBlackStatusBar() {
        window.statusBarColor = getColor(R.color.black)
    }

    fun setOnBackClicked(webView: WebView) {
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    }
                }
            })
    }
}