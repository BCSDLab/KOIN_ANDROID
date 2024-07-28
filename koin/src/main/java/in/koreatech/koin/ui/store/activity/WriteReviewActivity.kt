package `in`.koreatech.koin.ui.store.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.RatingBar
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.internal.TextWatcherAdapter
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ActivityWriteReviewBinding
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.ui.store.adapter.review.MenuImageRecyclerViewAdapter
import `in`.koreatech.koin.ui.store.adapter.review.MenuRecyclerViewAdapter
import `in`.koreatech.koin.ui.store.viewmodel.WriteReviewViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteReviewBinding
    private val viewModel by viewModels<WriteReviewViewModel>()
    private val menuRecyclerViewAdapter = MenuRecyclerViewAdapter()
    private val menuImageRecyclerViewAdapter = MenuImageRecyclerViewAdapter { position ->
        viewModel.deleteMenuImage(position)
    }
    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            uris.forEach { uri ->
                val inputStream = this.contentResolver.openInputStream(uri)
                if (uri.scheme.equals("content")) {
                    val cursor = this.contentResolver.query(uri, null, null, null, null)
                    cursor.use {
                        if (cursor != null && cursor.moveToFirst()) {
                            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                            if (fileNameIndex != -1 && fileSizeIndex != -1) {
                                val fileName = cursor.getString(fileNameIndex)
                                val fileSize = cursor.getLong(fileSizeIndex)

                                if (inputStream != null) {
                                    viewModel.getPreSignedUrl(
                                        fileSize,
                                        "image/" + fileName.split(".")[1],
                                        fileName,
                                        uri.toString()
                                    )
                                }
                                inputStream?.close()
                            }
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initView() {
        val storeName = intent.getStringExtra("storeName")
        val storeId = intent.getIntExtra("storeId", -1)
        with(binding) {
            storeNameTextView.text = storeName


            starRating.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    binding.ratingNumber.text = rating.toString()
                }
            writeReviewButton.setOnClickListener {
                viewModel.writeReview(
                    storeId, Review(
                        starRating.rating.toInt(), reviewEditText.text.toString(),
                        viewModel.menuImageUrls.value, viewModel.menuList.value,
                    )
                )
            }
            uploadImageButton.setOnClickListener {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            addMenuButton.setOnClickListener {
                menuRecyclerViewAdapter.addMenu()
            }
            writeReviewButton.setOnClickListener{
                viewModel.writeReview(
                    storeId, Review(
                        starRating.rating.toInt(), reviewEditText.text.toString(),
                        viewModel.menuImageUrls.value, viewModel.menuList.value,
                    )
                )
                finish();
            }
            menuRecyclerView.adapter = menuRecyclerViewAdapter
            imageRecyclerView.adapter = menuImageRecyclerViewAdapter

            reviewEditText.addTextChangedListener(@SuppressLint("RestrictedApi")
            object : TextWatcherAdapter() {
                var maxText = ""
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    super.beforeTextChanged(s, start, count, after)
                    maxText = s.toString()
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    super.onTextChanged(s, start, before, count)
                    charactersNumber.text = "${reviewEditText.length()}/500"
                    if (s.length > 500) {
                        reviewEditText.setText(maxText)
                        reviewEditText.setSelection(maxText.length - 1)
                        charactersNumber.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.colorAccent
                            )
                        )
                    } else {
                        charactersNumber.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.gray18
                            )
                        )
                    }

                }
            })

        }
    }

    private fun initViewModel() = with(viewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                menuImageUrls.collect {
                    menuImageRecyclerViewAdapter.submitList(it)

                    if (it.isNotEmpty()) binding.imageContainer.visibility =
                        android.view.View.VISIBLE else binding.imageContainer.visibility =
                        android.view.View.GONE

                    binding.imageNumber.text = "${it.size}/3"
                    binding.imageNumber.setTextColor(
                        if (it.size == 3)
                            ContextCompat.getColor(this@WriteReviewActivity, R.color.colorAccent)
                        else
                            ContextCompat.getColor(this@WriteReviewActivity, R.color.gray18)
                    )

                }
            }
        }
    }
}
