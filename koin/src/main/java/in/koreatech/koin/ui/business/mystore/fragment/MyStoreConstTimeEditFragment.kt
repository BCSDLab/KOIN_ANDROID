package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.constant.MyStoreEnum
import `in`.koreatech.koin.databinding.MystoreEditConstTimeFragmentBinding
import `in`.koreatech.koin.ui.business.mystore.MyStoreActivity
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel

class MyStoreConstTimeEditFragment : Fragment() {
    private var _binding: MystoreEditConstTimeFragmentBinding? = null
    private val binding: MystoreEditConstTimeFragmentBinding
        get() = _binding!!
    private val viewModel by activityViewModels<MyStoreViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MystoreEditConstTimeFragmentBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mondayTimeTextview.setOnClickListener { onClickTime() }
        }
    }

    override fun onResume() {
        super.onResume()
        onBackPressed()
    }

    private fun onClickTime() {
        viewModel.changeMyStoreState(MyStoreEnum.TIME_EDIT)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val frag =
                        parentFragmentManager.findFragmentByTag(MyStoreActivity.MyStoreTAG.MY_STORE_CONST_TIME_EDIT_TAG)
                    if (frag != null) {
                        parentFragmentManager.beginTransaction()
                            .remove(frag)
                            .commit()
                    }
                }
            })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): MyStoreConstTimeEditFragment {
            val args = Bundle()

            val fragment = MyStoreConstTimeEditFragment()
            fragment.arguments = args
            return fragment
        }
    }
}