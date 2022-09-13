package `in`.koreatech.koin.ui.dining.adapter

import `in`.koreatech.koin.data.mapper.toLineChangingString
import `in`.koreatech.koin.databinding.DiningListItemBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DiningRecyclerViewAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : RecyclerView.Adapter<DiningRecyclerViewAdapter.DiningViewHolder>() {
    private var diningData: List<Dining> = listOf()

    inner class DiningViewHolder(val binding: DiningListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiningViewHolder =
        DiningViewHolder(DiningListItemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: DiningViewHolder, position: Int) {
        with(holder.binding) {
            this.diningItemTitle.text = diningData[position].place
            this.diningItemInfo.text = diningData[position].kcal
            this.diningItemPrice.text = diningData[position].price
            this.diningItemMenu.text = diningData[position].menu.toLineChangingString()
        }
    }

    override fun getItemCount() = diningData.size

    fun setData(data: List<Dining>) {
        diningData = data
        notifyDataSetChanged()
    }
}