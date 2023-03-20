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

class StoreDetailFlyerRecyclerAdapter(
) : ListAdapter<String, StoreDetailFlyerRecyclerAdapter.ViewHolder>(
    diffCallback
) {
    var onItemClickListener: OnItemClickListener? = null

    private val glideOptions: RequestOptions = RequestOptions()
        .fitCenter()
        .override(300, 300)
        .error(R.drawable.image_no_image)
        .placeholder(R.color.white)

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flyerImageview: ImageView = itemView.findViewById(R.id.store_flyer_imageview)

        fun bind(position: Int, url: String) {
            Glide.with(flyerImageview)
                .load(url)
                .apply(glideOptions)
                .into(flyerImageview)

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(position, url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.store_flyer_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (position: Int, url: String) -> Unit) {
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, url: String) {
                onItemClick(position, url)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, url: String)
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