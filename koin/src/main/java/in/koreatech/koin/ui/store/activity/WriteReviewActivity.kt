package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.RatingBar
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
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
            menuRecyclerView.adapter = menuRecyclerViewAdapter
            imageRecyclerView.adapter = menuImageRecyclerViewAdapter

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
                }
            }
        }
    }
}
