package `in`.koreatech.koin.ui.store.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.StoreListItemBinding
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.util.ext.HHMM
import `in`.koreatech.koin.domain.util.ext.isEqualOrBigger
import `in`.koreatech.koin.domain.util.ext.isEqualOrSmaller
import `in`.koreatech.koin.domain.util.ext.localTimeNow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
            binding.storeDeliveryTextview.setTextState(store.isDeliveryOk)
            binding.storeCardTextview.setTextState(store.isCardOk)
            binding.storeAccountTextview.setTextState(store.isBankOk)
            binding.readyStoreFrameLayout.isVisible = if (store.open.closed) {
                true
            } else {
                !store.open.openStore()
            }

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
                    if (active) R.color.colorAccent else R.color.blue1
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