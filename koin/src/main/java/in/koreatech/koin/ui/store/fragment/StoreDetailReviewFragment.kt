package `in`.koreatech.koin.ui.store.fragment

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.databinding.FragmentStoreDetailReviewBinding
import `in`.koreatech.koin.ui.store.activity.WriteReviewActivity
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.store.ReviewFilterEnum
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.ui.store.activity.StoreReviewReportActivity
import `in`.koreatech.koin.ui.store.adapter.StoreDetailEventRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.review.StoreDetailReviewRecyclerAdapter
import `in`.koreatech.koin.ui.store.dialog.ReviewDeleteCheckDialog
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailReviewFragment : Fragment() {
    private var _binding: FragmentStoreDetailReviewBinding? = null
    private val binding: FragmentStoreDetailReviewBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val storeDetailReviewRecyclerAdapter by lazy {
        StoreDetailReviewRecyclerAdapter(
            onModifyItem = {
                val goToReviewScreen = Intent(requireContext(), WriteReviewActivity::class.java)
                goToReviewScreen.putExtra("storeId", viewModel.store.value?.uid)
                goToReviewScreen.putExtra("storeName", viewModel.store.value?.name)
                goToReviewScreen.putExtra("review", it)
                startActivity(goToReviewScreen)

            },
            onDeleteItem = {
                val reviewDeleteDialog = ReviewDeleteCheckDialog(
                    onDelete = {
                        viewModel.deleteReview(it, viewModel.store.value!!.uid)
                        viewModel.getShopReviews(viewModel.store.value!!.uid)
                    }
                )
                reviewDeleteDialog.show(childFragmentManager, "ReviewDeleteCheckDialog")
            },
            onReportItem ={
                val intent = Intent(requireContext(), StoreReviewReportActivity::class.java)
                intent.putExtra("storeId", viewModel.store.value?.uid)
                intent.putExtra("reviewId", it.reviewId)
                startActivity(intent)
            }
        )
    }

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

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {

        with(binding){

            reviewContentRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storeDetailReviewRecyclerAdapter
            }

            storeDetailReviewButton.setOnClickListener {
                val goToReviewScreen = Intent(requireContext(), WriteReviewActivity::class.java)
                goToReviewScreen.putExtra("storeName", viewModel.store.value?.name)
                goToReviewScreen.putExtra("storeId", viewModel.store.value?.uid)
                startActivity(goToReviewScreen)
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
                            viewModel.filterReview(ReviewFilterEnum.LATEST, isMineCheckbox.isChecked)
                            filterTextview.text = getString(R.string.latest)
                            true
                        }
                        R.id.action_oldest -> {
                            viewModel.filterReview(ReviewFilterEnum.OLDEST, isMineCheckbox.isChecked)
                            filterTextview.text = getString(R.string.oldest)
                            true
                        }
                        R.id.action_high_rating -> {
                            viewModel.filterReview(ReviewFilterEnum.HIGH_RATTING, isMineCheckbox.isChecked)
                            filterTextview.text = getString(R.string.high_rating)
                            true
                        }
                        R.id.action_low_rating -> {
                            viewModel.filterReview(ReviewFilterEnum.LOW_RATIONG, isMineCheckbox.isChecked)
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