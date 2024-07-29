package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivityDiningNoticeBinding
import `in`.koreatech.koin.ui.dining.viewmodel.DiningNoticeViewModel

class DiningNoticeActivity : ActivityBase() {
    private lateinit var binding: ActivityDiningNoticeBinding
    private val viewModel by viewModels<DiningNoticeViewModel>()
    override val screenTitle: String
        get() = "학생식당정보"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiningNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상하단 system bar 고려해서 화면 구성
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        with(binding) {
            btnDiningNoticeTopbarBack.setOnClickListener {
                finish()
            }
        }
    }
}