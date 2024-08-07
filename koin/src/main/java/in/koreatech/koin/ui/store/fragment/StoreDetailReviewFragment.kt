package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ScrollView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentStoreDetailReviewBinding
import `in`.koreatech.koin.domain.model.store.ReviewFilterEnum
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.ui.store.adapter.StoreDetailEventRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.review.StoreDetailReviewRecyclerAdapter
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailReviewFragment : Fragment() {
    private var _binding: FragmentStoreDetailReviewBinding? = null
    private val binding: FragmentStoreDetailReviewBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val storeDetailReviewRecyclerAdapter = StoreDetailReviewRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStoreDetailReviewBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {

        with(binding){

            reviewContentRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storeDetailReviewRecyclerAdapter
            }

            observeLiveData(viewModel.storeReview) {

                scoreText.text = String.format("%.1f", it.statistics.averageRating)
                storeTotalRating.rating = it.statistics.averageRating.toFloat()

                ratingFiveCountTv.text  = it.statistics.ratings["5"].toString()
                ratingFiveProgressbar.progress = calculateScore(it.totalCount, it.statistics.ratings["5"])

                ratingFourCountTv.text  = it.statistics.ratings["4"].toString()
                ratingFourProgressbar.progress = calculateScore(it.totalCount, it.statistics.ratings["4"])

                ratingThreeCountTv.text  = it.statistics.ratings["3"].toString()
                ratingThreeProgressbar.progress = calculateScore(it.totalCount, it.statistics.ratings["3"])

                ratingTwoCountTv.text  = it.statistics.ratings["2"].toString()
                ratingTwoProgressbar.progress = calculateScore(it.totalCount, it.statistics.ratings["2"])

                ratingOneCountTv.text  = it.statistics.ratings["1"].toString()
                ratingOneProgressbar.progress = calculateScore(it.totalCount, it.statistics.ratings["1"])


                if(it.totalCount == 0) {
                    yesReviewLayout.isGone= true
                    noReviewLayout.isVisible = true
                }
                else {
                    yesReviewLayout.isVisible = true
                    noReviewLayout.isGone = true
                }
            }

            filterLayout.setOnClickListener {
                val popup = PopupMenu(filterLayout.context, filterLayout)
                popup.menuInflater.inflate(R.menu.review_filter_menu, popup.menu)

                popup.setOnMenuItemClickListener { item: MenuItem ->
                    when (item.itemId) {
                        R.id.action_latest -> {
                            viewModel.filterReview(ReviewFilterEnum.LATEST)
                            filterTextview.text = getString(R.string.latest)
                            true
                        }
                        R.id.action_oldest -> {
                            viewModel.filterReview(ReviewFilterEnum.OLDEST)
                            filterTextview.text = getString(R.string.oldest)
                            true
                        }
                        R.id.action_high_rating -> {
                            viewModel.filterReview(ReviewFilterEnum.HIGH_RATTING)
                            filterTextview.text = getString(R.string.high_rating)
                            true
                        }
                        R.id.action_low_rating -> {
                            viewModel.filterReview(ReviewFilterEnum.LOW_RATIONG)
                            filterTextview.text = getString(R.string.low_rating)
                            true
                        }
                        else -> false
                    }
                }

                popup.show()
            }

            isMineCheckbox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.checkShowMyReview(isChecked)
            }

        }

    }

    private fun initViewModel() {
        with(viewModel){
            observeLiveData(storeReviewContent) {
                storeDetailReviewRecyclerAdapter.submitList(it)
            }

            observeLiveData(store){
                storeDetailReviewRecyclerAdapter.storeId = store.value?.uid
            }

            observeLiveData(scrollUp){
                if(it == StoreDetailScrollType.REVIEW){
                    binding.reviewScrollView.fullScroll(ScrollView.FOCUS_UP)
                    viewModel.scrollReset()
                }
            }
        }
    }

    private fun calculateScore(total: Int, count: Int?) = count?.let { ((it.toFloat() / total) * 100).toInt() } ?: 0
}