package `in`.koreatech.koin.ui.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.R

class StoreDetailFlyerFullRecyclerAdapter(
) : ListAdapter<String, StoreDetailFlyerFullRecyclerAdapter.ViewHolder>(
    diffCallback
) {
    private val glideOptions: RequestOptions = RequestOptions()
        .fitCenter()
        .error(R.drawable.image_no_image)
        .placeholder(R.color.white)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flyerImageview: ImageView = itemView.findViewById(R.id.store_flyer_image_view_scalable)

        fun bind(url: String) {
            Glide.with(flyerImageview)
                .load(url)
                .apply(glideOptions)
                .into(flyerImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.store_flyer_list_item_full, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
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