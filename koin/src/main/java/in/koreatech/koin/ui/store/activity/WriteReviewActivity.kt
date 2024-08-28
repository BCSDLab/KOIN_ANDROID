package `in`.koreatech.koin.ui.store.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputFilter
import android.view.View
import android.widget.RatingBar
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.internal.TextWatcherAdapter
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivityWriteReviewBinding
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import `in`.koreatech.koin.ui.store.adapter.review.MenuImageRecyclerViewAdapter
import `in`.koreatech.koin.ui.store.adapter.review.MenuRecyclerViewAdapter
import `in`.koreatech.koin.ui.store.viewmodel.WriteReviewViewModel
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class WriteReviewActivity : ActivityBase(R.layout.activity_write_review) {
    private lateinit var binding: ActivityWriteReviewBinding
    override val screenTitle = "리뷰 작성"
    private val viewModel by viewModels<WriteReviewViewModel>()
    private val menuRecyclerViewAdapter = MenuRecyclerViewAdapter()
    private val menuImageRecyclerViewAdapter = MenuImageRecyclerViewAdapter { position ->
        viewModel.deleteMenuImage(position)
    }
    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            uris.take(3 - viewModel.menuImageUrls.value.size).forEach { uri ->
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
        val review = intent.intentSerializable("review", StoreReviewContent::class.java)
        with(binding) {
            koinBaseAppBar.leftButton.setOnClickListener {
                finish()
            }

            storeNameTextView.text = storeName
            starRating.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    if (rating < 1) {
                        binding.ratingNumber.text = "1"
                        starRating.rating = 1f
                    } else binding.ratingNumber.text = rating.toInt().toString()
                }

            if (review != null) {
                starRating.rating = review.rating.toFloat()
                charactersNumber.text = "${review.content.length}/500"
                reviewEditText.setText(review.content)
                menuRecyclerViewAdapter.submitList(review.menuNames)
                viewModel.setImage(review.imageUrls)
            }

            uploadImageButton.debounce(300, lifecycleScope) {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            addMenuButton.setOnClickListener {
                menuRecyclerViewAdapter.addMenu()
            }
            menuRecyclerView.adapter = menuRecyclerViewAdapter
            imageRecyclerView.adapter = menuImageRecyclerViewAdapter
            reviewEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(500))

            reviewEditText.addTextChangedListener(@SuppressLint("RestrictedApi")
            object : TextWatcherAdapter() {
                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    super.onTextChanged(s, start, before, count)

                    charactersNumber.text = "${reviewEditText.length()}/500"
                    if (s.length >= 500) {
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
                                R.color.gray19
                            )
                        )
                    }

                }
            })
            writeReviewButton.debounce(300, lifecycleScope) {
                if (review?.reviewId != null) {
                    viewModel.modifyReview(
                        review.reviewId, storeId, Review(
                            starRating.rating.toInt(), reviewEditText.text.toString(),
                            viewModel.menuImageUrls.value, menuRecyclerViewAdapter.getMenuList()
                        )
                    )
                } else {
                    viewModel.writeReview(
                        storeId, Review(
                            starRating.rating.toInt(), reviewEditText.text.toString(),
                            viewModel.menuImageUrls.value, menuRecyclerViewAdapter.getMenuList()
                        )
                    )
                }
                finish()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        withLoading(this@WriteReviewActivity, this)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                menuImageUrls.collect {
                    menuImageRecyclerViewAdapter.submitList(it)
                    if (it.isNotEmpty()) binding.imageContainer.visibility =
                        View.VISIBLE else binding.imageContainer.visibility =
                        View.GONE

                    if (viewModel.menuImageUrls.value.size >= 3) {
                        binding.uploadImageButton.isEnabled = false
                        binding.uploadImageButton.alpha = 0.5f
                        binding.uploadImageButton.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.gray19
                            )
                        )
                        binding.imageNumber.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.colorAccent
                            )
                        )
                    } else {
                        binding.uploadImageButton.isEnabled = true
                        binding.uploadImageButton.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.gray14
                            )
                        )
                        binding.uploadImageButton.alpha = 1f
                        binding.imageNumber.setTextColor(
                            ContextCompat.getColor(
                                this@WriteReviewActivity,
                                R.color.gray19
                            )
                        )
                    }
                    binding.imageNumber.text = "${it.size}/3"
                }
            }
        }
    }

    fun View.debounce(
        delayMillis: Long = 300L,
        scope: CoroutineScope,
        action: (Unit) -> Unit
    ) {
        var job: Job? = null
        this.setOnClickListener {
            job?.cancel()
            job = scope.launch {
                delay(delayMillis)
                action(Unit)
            }
        }
    }

    fun <T : Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }
}
