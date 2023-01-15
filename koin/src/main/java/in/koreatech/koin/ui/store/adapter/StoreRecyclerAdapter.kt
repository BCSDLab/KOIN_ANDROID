package `in`.koreatech.koin.ui.store.adapter

import android.content.Context
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
import `in`.koreatech.koin.domain.util.ext.isCurrentOpen
import kotlin.math.roundToInt

class StoreRecyclerAdapter : ListAdapter<Store, StoreRecyclerAdapter.ViewHolder>(
    diffCallback
) {

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

    class ViewHolder(private val binding: StoreListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(store: Store) {
            binding.storeNameTextview.text = store.name
            binding.storeDeliveryTextview.setTextState(store.isDeliveryOk)
            binding.storeCardTextview.setTextState(store.isCardOk)
            binding.storeAccountTextview.setTextState(store.isBankOk)
            binding.readyStoreFrameLayout.isVisible = store.isCurrentOpen
        }

        /**
         * @param active true -> colorAccent, false -> blue1
         */
        fun TextView.setTextState(active: Boolean) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (active) R.color.colorAccent else R.color.blue1
                )
            )
        }

        fun dpToPx(context: Context, dp: Int): Int {
            val density: Float = context.resources.displayMetrics.density
            return (dp.toFloat() * density).roundToInt()
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Store>() {
            override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
                return oldItem == newItem
            }
        }
    }
}