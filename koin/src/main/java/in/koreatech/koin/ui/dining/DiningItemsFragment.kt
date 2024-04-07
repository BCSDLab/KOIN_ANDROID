package `in`.koreatech.koin.ui.dining

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.dining.toDiningType
import `in`.koreatech.koin.ui.dining.adapter.DiningTypeAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by activityViewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDiningType.apply {
            adapter = DiningTypeAdapter()
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dining.collect {
                    (binding.recyclerViewDiningType.adapter as DiningTypeAdapter).submitList(it[type])
                }
            }
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