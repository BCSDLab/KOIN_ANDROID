package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.dialog.ImageZoomableDialog

class StoreDetailImageViewpagerAdapter(private val images: List<String>?):
    RecyclerView.Adapter<StoreDetailImageViewpagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store_detail_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images?.get(position))
    }

    override fun getItemCount(): Int {
        return images?.size ?: 0
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.store_detail_image_imageview)

        fun bind(imageRes: String?) {
            val context = itemView.context

            Glide.with(itemView)
                .load(imageRes ?: R.drawable.image_no_image)
                .into(imageView)

            itemView.setOnClickListener {
                imageRes?.let { ImageZoomableDialog(context, imageRes) }
                    .also { it?.show() }
            }
        }
    }
}
