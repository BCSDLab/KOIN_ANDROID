package `in`.koreatech.koin.ui.storeregistermanual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.StoreRegistrationManual
import `in`.koreatech.koin.databinding.StoreRegistrationManualFragmentBinding

class StoreRegistrationManualFragment : Fragment() {
    lateinit var binding : StoreRegistrationManualFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.store_registration_manual_fragment, container, false)
        binding.goRegisterButton.setOnClickListener {
            //액티비티 이동
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf {
            it.containsKey(StoreRegistrationManual.FRAGMENT_PAGE_KEY)
        }?.apply {
            when (getInt(StoreRegistrationManual.FRAGMENT_PAGE_KEY)) {
                1 -> {
                    binding.manualImageView.setImageResource(R.drawable.image_store_registration_manual_1)
                    binding.manualTitleTextView.text = getText(R.string.store_registration_manual_page_1_title)
                    binding.manualContentTextView.text = getText(R.string.store_registration_manual_page_1_content)
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_1)
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                }
                2 -> {
                    binding.manualImageView.setImageResource(R.drawable.image_store_registration_manual_2)
                    binding.manualTitleTextView.text = getText(R.string.store_registration_manual_page_2_title)
                    binding.manualContentTextView.text = getText(R.string.store_registration_manual_page_2_content)
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_2)
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                }
                3 -> {
                    binding.manualImageView.setImageResource(R.drawable.image_store_registration_manual_3)
                    binding.manualTitleTextView.text = getText(R.string.store_registration_manual_page_3_title)
                    binding.manualContentTextView.text = getText(R.string.store_registration_manual_page_3_content)
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_3)
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                }
                4 -> {
                    binding.manualImageView.setImageResource(R.drawable.image_store_registration_manual_4)
                    binding.manualTitleTextView.text = getText(R.string.store_registration_manual_page_4_title)
                    binding.manualContentTextView.text = getText(R.string.store_registration_manual_page_4_content)
                    binding.dotsImageView.visibility = View.GONE
                    binding.goRegisterButton.visibility = View.VISIBLE
                }
            }
        }
    }
}