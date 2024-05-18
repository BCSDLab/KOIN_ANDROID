package `in`.koreatech.koin.ui.store.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreListItemBinding
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.util.ext.hasJongSungAtLastChar

class StoreRecyclerAdapter : ListAdapter<Store, StoreRecyclerAdapter.ViewHolder>(
    diffCallback
) {
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StoreListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: StoreListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.storeNameTextview.text = store.name
            binding.storeNameTextview.setStoreNameState(store.isOpen)
            binding.storeDeliveryTextview.setTextState(store.isDeliveryOk)
            binding.storeCardTextview.setTextState(store.isCardOk)
            binding.storeAccountTextview.setTextState(store.isBankOk)
            if(!store.isOpen){
                binding.readyStoreFrameLayout.isVisible = true
                if(store.name.hasJongSungAtLastChar()){
                    val fullText = itemView.context.getString(R.string.store_eun, store.name)
                    val spannableString = SpannableString(fullText)

                    val start = fullText.indexOf(store.name)
                    val end = start + store.name.length

                    val color = ContextCompat.getColor(itemView.context, R.color.closed_store_name)

                    if (start >= 0) {
                        spannableString.setSpan(
                            ForegroundColorSpan(color),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    binding.storeDoesNotOpenTextView.text = spannableString
                } else {
                    val fullText = itemView.context.getString(R.string.store_neun, store.name)

                    val spannableString = SpannableString(fullText)

                    val start = fullText.indexOf(store.name)
                    val end = start + store.name.length

                    val color = ContextCompat.getColor(itemView.context, R.color.closed_store_name)

                    if (start >= 0) {
                        spannableString.setSpan(
                            ForegroundColorSpan(color),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }

                    binding.storeDoesNotOpenTextView.text = spannableString
                }
            }
            else{
                binding.readyStoreFrameLayout.isInvisible = true
            }

            binding.eventImageView.isVisible = store.isEvent

            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(store)
            }
        }

        /**
         * @param active true -> colorAccent, false -> blue1
         */
        private fun TextView.setTextState(active: Boolean) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (active) R.color.colorPrimary else R.color.blue1
                )
            )
        }

        private fun TextView.setStoreNameState(active: Boolean) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (!active) R.color.blue1 else R.color.black
                )
            )
        }
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (store: Store) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(store: Store) {
                onItemClick(store)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(store: Store)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Store>() {
            override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem == newItem
            }
        }
    }
}