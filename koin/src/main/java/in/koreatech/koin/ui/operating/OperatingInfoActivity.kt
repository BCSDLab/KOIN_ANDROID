package `in`.koreatech.koin.ui.operating

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ActivityOperatingInfoBinding

class OperatingInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperatingInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOperatingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initWebView()
        initToolbar()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webviewOperatingInfo.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            if (BuildConfig.IS_DEBUG)
                loadUrl(getString(R.string.koreatech_operating_info_stage_url))
            else
                loadUrl(getString(R.string.koreatech_operating_info_production_url))

        }
    }

    private fun initToolbar() {
        binding.toolbarOperatingInfo.apply {
            setTitle(getString(R.string.navigation_item_koreatech_operating_information))
            setOnNavigationIconClickListener { finish() }
        }
    }
}