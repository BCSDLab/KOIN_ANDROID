package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessCertificationBinding
import `in`.koreatech.koin.ui.businesssignup.fragment.AttachmentDialogFragment
import `in`.koreatech.koin.util.ext.textString
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class BusinessCertificationActivity : AppCompatActivity() {
    private val binding by dataBinding<ActivityBusinessCertificationBinding>()
    private var mFileChooserCallback: ValueCallback<Array<Uri>>? = null
    private var allWriteCheck = false
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_certification)

        initView()
    }

    private fun initView() = with(binding) {
        allWriteCheck = if(isAllWrite()) {
            businessCertificationNextButton.setBackgroundColor(getColor(R.color.colorPrimary))
            true
        } else {
            businessCertificationNextButton.setBackgroundColor(getColor(R.color.gray5))
            false
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val storeName = result.data?.getStringExtra("storeName")?: ""
                editStoreNameText.setText(storeName)
            }
        }
        searchStoreButton.setOnClickListener {
            val intent = Intent(this@BusinessCertificationActivity, BusinessSearchStoreActivity::class.java)
            resultLauncher.launch(intent)
        }

        businessCertificationNextButton.setOnClickListener {
            if(allWriteCheck) startActivity(Intent(this@BusinessCertificationActivity, BusinessSignUpCompleteActivity::class.java))
            else Toast.makeText(this@BusinessCertificationActivity, getString(R.string.not_enter_all_items), Toast.LENGTH_SHORT).show()
        }

        attachFileButton.setOnClickListener {
            val dialog = AttachmentDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }

    private fun isAllWrite(): Boolean{
        if(binding.editBusinessmanNameText.text.toString().isBlank()) return false
        if(binding.editStoreNameText.text.toString().isBlank()) return false
        if(binding.editRegistrationNumberText.text.toString().isBlank()) return false
        if(binding.editPersonalContactText.text.toString().isBlank()) return false
        if(mFileChooserCallback == null) return false

        return true
    }
}