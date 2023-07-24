package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterMyStoreFragmentBinding
import `in`.koreatech.koin.ui.business.registerstore.activity.RegisterStoreActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class RegisterStoreFragment : Fragment(R.layout.register_my_store_fragment){
    private val binding by dataBinding<RegisterMyStoreFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.initStoreButton.setOnClickListener{
            findNavController().navigate(R.id.register_store_fragment_to_register_category_fragment)
        }
    }
}
