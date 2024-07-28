package `in`.koreatech.koin.ui.store.adapter.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemWriteReviewImageBinding

class MenuImageRecyclerViewAdapter(
    private val onDeleteClick: (Int) -> Unit
) :
    ListAdapter<String, MenuImageRecyclerViewAdapter.MenuImageRecyclerViewHolder>(
        diffCallback
    ) {

    inner class MenuImageRecyclerViewHolder(val binding: ItemWriteReviewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuImageRecyclerViewAdapter.MenuImageRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWriteReviewImageBinding.inflate(inflater, parent, false)
        return MenuImageRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MenuImageRecyclerViewAdapter.MenuImageRecyclerViewHolder,
        position: Int
    ) {
        val imageUri = getItem(position)
        with(holder) {
            binding.writeReviewImageView.clipToOutline = true

            Glide.with(binding.writeReviewImageView)
                .load(imageUri)
                .override(200, 200)
                .error(R.drawable.image_no_image)
                .into(binding.writeReviewImageView)

            binding.deleteImageButton.setOnClickListener {
                val updatedList = currentList.filterIndexed { index, _ -> index != position }
                submitList(updatedList) {
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                    onDeleteClick(position)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}