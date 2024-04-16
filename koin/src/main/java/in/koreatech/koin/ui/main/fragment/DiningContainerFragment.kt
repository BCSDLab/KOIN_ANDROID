package `in`.koreatech.koin.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningContainerBinding
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel

class DiningContainerFragment : Fragment(R.layout.fragment_dining_container) {
    private val binding by dataBinding<FragmentDiningContainerBinding>()
    private val viewModel by viewModels<MainActivityViewModel>()
    private val place by lazy { arguments?.getString(PLACE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        place.apply {
            binding.textViewDiningContainer.text = this.toString()
        }
    }

    companion object {
        private const val PLACE = "place"
        fun newInstance(place: String) =
            DiningContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(PLACE, place)
                }
            }
    }
}