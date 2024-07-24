package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.StoreActivityDetailBinding
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.ui.store.fragment.StoreFlyerDialogFragment
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.ui.store.viewmodel.WriteReviewViewModel

class ActivityWriteReview : AppCompatActivity() {

 //   private val binding by dataBinding<StoreActivityDetailBinding>(R.layout.store_activity_detail)
    private val viewModel by viewModels<WriteReviewViewModel>()
    private var flyerDialogFragment: StoreFlyerDialogFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)
        viewModel.writeReview(200, Review(1, "test", null, listOf("오징어땅콩")))

    }
}