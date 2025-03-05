package `in`.koreatech.koin.ui.businesssignup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.AttachStoreItemBinding
import `in`.koreatech.koin.domain.model.store.AttachStore

class AttachStoreImageAdapter : ListAdapter<AttachStore, AttachStoreImageAdapter.ViewHolder>(diffCallback) {
    inner class ViewHolder(private val binding: AttachStoreItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(store: AttachStore) {
            binding.storeNameTextView.text = store.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            AttachStoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    fun updateList(storeTitleList: List<AttachStore>) {
        submitList(storeTitleList)
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<AttachStore>() {
                override fun areItemsTheSame(
                    oldItem: AttachStore,
                    newItem: AttachStore,
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(
                    oldItem: AttachStore,
                    newItem: AttachStore,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
