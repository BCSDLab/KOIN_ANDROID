package `in`.koreatech.koin.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.databinding.MainItemStoreBinding
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.store.StoreCategories

class StoreCategoriesRecyclerAdapter :RecyclerView.Adapter<StoreCategoriesRecyclerAdapter .ViewHolder>(){

    private val storeCategories = mutableListOf<StoreCategories>()

    var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: MainItemStoreBinding) : RecyclerView.ViewHolder(binding.root){
        val container = binding.container
        val storeCategoryImage = binding.imageViewStoreCategory
        val storeCategoryName = binding.textViewStoreCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCategoriesRecyclerAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainItemStoreBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = storeCategories.size


    override fun onBindViewHolder(holder: StoreCategoriesRecyclerAdapter.ViewHolder, position: Int) {
        with(holder){
            container.setOnClickListener {
                onItemClickListener?.onItemClick(storeCategories[position].id)
            }

            Glide.with(storeCategoryImage)
                .load(storeCategories[position].image_url)
                .override(100, 100)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(storeCategoryImage)

            storeCategoryName.text = storeCategories[position].name
        }
    }

    fun setStoreCategoriesData(data: List<StoreCategories>){
        storeCategories.clear()
        storeCategories.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    inline fun setOnItemClickListener(crossinline onItemClick: (Id: Int) -> Unit) {
        onItemClickListener = object : StoreCategoriesRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                onItemClick(id)
            }
        }
    }
}