package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.ui.dining.adapter.DiningTypeAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by viewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDiningType.apply {
            adapter = DiningTypeAdapter()
        }
    }

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String) = DiningItemsFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }
    }
}