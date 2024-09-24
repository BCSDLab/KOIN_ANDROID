package `in`.koreatech.koin.ui.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
    private val onItemClick: (Int) -> Unit
) : ListAdapter<BenefitCategory, RecyclerView.ViewHolder>(
    diffCallback
) {

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(benefitCategory: BenefitCategory) {
            with(binding as ItemStoreBenefitBinding) {
                storeBenefitItemLayout.setOnClickListener {
                    onItemClick(benefitCategory.id)
                    Glide.with(binding.benefitIcon)
                        .load(benefitCategory.onImageUrl)
                        .into(binding.benefitIcon)
                }
                benefitTitle.text = benefitCategory.title

                Glide.with(binding.benefitIcon)
                    .load(benefitCategory.offImageUrl)
                    .error(R.drawable.ic_trash_can)
                    .into(binding.benefitIcon)

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