package `in`.koreatech.koin.ui.store.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.databinding.FragmentStoreDetailReviewBinding
import `in`.koreatech.koin.ui.store.activity.WriteReviewActivity
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

        with(binding) {
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
                else{
                    yesReviewLayout.isVisible= true
                    noReviewLayout.isGone = true
                }
            }
        }

    }

    private fun initViewModel() {
        observeLiveData(viewModel.storeReview) {
            storeDetailReviewRecyclerAdapter.submitList(it.reviews)
        }

    }

    private fun calculateScore(total: Int, count: Int?) = count?.let { ((it.toFloat() / total) * 100).toInt() } ?: 0
}