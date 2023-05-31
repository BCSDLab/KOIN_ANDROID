package `in`.koreatech.koin.ui.business.mystore.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.MystoreMainFragmentBinding
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import `in`.koreatech.koin.ui.business.mystore.viewmodel.MyStoreViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class MyStoreMainFragment: Fragment(R.layout.mystore_main_fragment) {
    private val binding by dataBinding<MystoreMainFragmentBinding>()
    private val viewModel by activityViewModels<MyStoreViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        binding.myStoreEditContent.setOnClickListener {
            findNavController().navigate(R.id.action_mystore_main_fragment_to_mystore_edit_fragment)
        }
    }

    private fun observeData(){
        observeLiveData(viewModel.stores){ store ->
            with(binding){
                myStoreDetailTitleTextview.text = store.name
                myStoreDetailPhoneTextview.text = store.phoneNumber
                myStoreDetailTimeTextview.text = "${store.openTime} ~ ${store.closeTime}"
                myStoreDetailHolidayTextview.text = "매주" + store.dayOfHoliday
                myStoreDetailAddressTextview.text = store.address
                myStoreDetailDeliverTextview.text = "${store.deliveryPrice}원"
                myStoreDetailEtcTextview.text = store.description
            }
        }
    }
}