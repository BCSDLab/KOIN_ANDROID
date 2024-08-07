package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivityDiningNoticeBinding
import `in`.koreatech.koin.ui.dining.adapter.DiningNoticeAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningNoticeViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class DiningNoticeActivity : ActivityBase() {
    private lateinit var binding: ActivityDiningNoticeBinding
    private val viewModel by viewModels<DiningNoticeViewModel>()
    private val diningNoticeAdapter = DiningNoticeAdapter()
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
        initViewModel()
    }

    private fun initView() {
        with(binding) {
            btnDiningNoticeTopbarBack.setOnClickListener {
                finish()
            }
            rvDiningNotice.apply {
                layoutManager = LinearLayoutManager(this@DiningNoticeActivity)
                adapter = diningNoticeAdapter
                itemAnimator = null
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(diningNotice) {
            with(binding) {
                tvDiningNoticeTitle.text =
                    getString(R.string.dining_notice_title_format, it.name, it.semester)
                tvDiningNoticeLocationValue.text = it.location
                tvDiningNoticeTelValue.text = it.phone
                tvDiningNoticeUpdate.text =
                    getString(R.string.dining_notice_updated_at_format, it.updatedAt)
            }
            diningNoticeAdapter.submitList(it.opens)
        }
    }
}