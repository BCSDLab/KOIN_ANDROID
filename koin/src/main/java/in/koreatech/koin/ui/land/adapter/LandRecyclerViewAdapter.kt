package `in`.koreatech.koin.ui.land.adapter

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.LAND
import `in`.koreatech.koin.databinding.LandRecyclerviewItemBinding
import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.ui.land.LandDetailActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LandRecyclerViewAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : RecyclerView.Adapter<LandRecyclerViewAdapter.LandViewHolder>() {
    private var landData: List<Land> = listOf()
    private var selectedPosition: Int? = null

    inner class LandViewHolder(val binding: LandRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LandViewHolder(
            LandRecyclerviewItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LandViewHolder, position: Int) {
        with(holder.binding) {
            if(position == selectedPosition) {
                container.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_rect_accent)
            } else {
                container.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_rect_blue1)
            }
            landNameTextview.text = landData[position].name
            monthFeeTextview.text = landData[position].monthlyFee
            charterFeeTextview.text = landData[position].charterFee
            root.setOnClickListener {
                val intent = Intent(context, LandDetailActivity::class.java)
                if (position != RecyclerView.NO_POSITION) {
                    intent.putExtra(LAND.LAND_EXTRA_NAME, landData[position].id)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, context.getString(R.string.land_can_not_find_land), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount() = landData.size

    fun setLandList(landList: List<Land>) {
        landData = landList
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        if (position < landData.size) {
            selectedPosition?.let {
                val previousPosition = it
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(position)
            } ?: run {
                selectedPosition = position
                notifyItemChanged(position)
            }
        }
    }
}