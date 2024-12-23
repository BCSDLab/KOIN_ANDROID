package `in`.koreatech.koin.ui.error

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.core.view.isVisible
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityErrorBinding
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.util.ExceptionHandlerUtil
import `in`.koreatech.koin.util.ext.goToKakaoTalkBcsdlabFriend

class ErrorActivity : ActivityBase() {
    private val binding by dataBinding<ActivityErrorBinding>(R.layout.activity_error)
    override val screenTitle: String = "에러"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    fun init() {
        if (BuildConfig.IS_DEBUG) {
            val stackTrace = intent.extras?.getString(ExceptionHandlerUtil.EXTRA_ERROR_TEXT) ?: ""
            Log.e("ErrorHandler", stackTrace)
            binding.errorTitleMessage.isVisible = false
            binding.errorInfoMessage.text = stackTrace
            binding.errorInfoMessage.gravity = Gravity.START
        }

        binding.errorHomeButton.setOnClickListener {
            onClickErrorHomeButton()
        }

        binding.errorKakaotalkButton.setOnClickListener {
            onClickKakaoTalk()
        }
    }

    private fun onClickErrorHomeButton() {
        val goToHomeIntent = Intent(this, SplashActivity::class.java)
        finish()
        startActivity(goToHomeIntent)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val goToHomeIntent = Intent(this, SplashActivity::class.java)
        startActivity(goToHomeIntent)
    }

    private fun onClickKakaoTalk() {
        goToKakaoTalkBcsdlabFriend()
    }
}