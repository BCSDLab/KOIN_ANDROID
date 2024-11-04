package `in`.koreatech.koin.ui.store.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ItemStoreBenefitBinding
import `in`.koreatech.koin.domain.model.store.BenefitCategory

class StoreBenefitRecyclerAdapter(
    private val onItemClick: (Int) -> Unit,
    private val getPosition: (Int) -> Unit,
) : ListAdapter<BenefitCategory, RecyclerView.ViewHolder>(
    diffCallback
) {
    private var currentId = 0
    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(benefitCategory: BenefitCategory) {
            with(binding as ItemStoreBenefitBinding) {
                if(currentId==benefitCategory.id){
                    storeBenefitItemLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.button_rect_primary_line_radius_5dp)
                    benefitTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary_500))
                    Glide.with(binding.root.context)
                        .load(benefitCategory.onImageUrl)
                        .into(binding.benefitIcon)
                }else{
                    storeBenefitItemLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.button_rect_gray18_radius_5dp)
                    benefitTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray20))
                    Glide.with(binding.root.context)
                        .load(benefitCategory.offImageUrl)
                        .into(binding.benefitIcon)
                }

                storeBenefitItemLayout.setOnClickListener {
                    currentId = benefitCategory.id
                    onItemClick(benefitCategory.id)
                    getPosition(getBindingAdapterPosition())
                    notifyDataSetChanged()
                }

                benefitTitle.text = benefitCategory.title

            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemStoreBenefitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    fun setCurrentId(id:Int){
        currentId = id
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<BenefitCategory>() {
            override fun areItemsTheSame(
                oldItem: BenefitCategory,
                newItem: BenefitCategory
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BenefitCategory,
                newItem: BenefitCategory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}