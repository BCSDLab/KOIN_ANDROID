package `in`.koreatech.koin.ui.article.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.ItemAttachmentBinding
import `in`.koreatech.koin.ui.article.state.AttachmentState

class AttachmentAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<AttachmentState, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val binding = ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttachmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AttachmentViewHolder).bind(getItem(position))
    }

    inner class AttachmentViewHolder(
        private val binding: ItemAttachmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attachment: AttachmentState) {
            binding.apply {
                textViewAttachmentTitle.text = attachment.title
                textViewAttachmentSize.text = attachment.size
                root.setOnClickListener {
                    onClick(attachment.url)
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<AttachmentState>() {
            override fun areItemsTheSame(oldItem: AttachmentState, newItem: AttachmentState): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: AttachmentState, newItem: AttachmentState): Boolean {
                return oldItem == newItem
            }
        }
    }
}