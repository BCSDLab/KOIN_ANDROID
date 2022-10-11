package `in`.koreatech.koin.ui.land.adapter

import `in`.koreatech.koin.constant.LAND
import `in`.koreatech.koin.databinding.LandDetailViewpagerPageBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LandDetailViewPagerAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : RecyclerView.Adapter<LandDetailViewPagerAdapter.LandDetailPagerViewHolder>() {
    var imageUrlList: List<String> = listOf()

    inner class LandDetailPagerViewHolder(val binding: LandDetailViewpagerPageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandDetailPagerViewHolder =
        LandDetailPagerViewHolder(
            LandDetailViewpagerPageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LandDetailPagerViewHolder, position: Int) {
        with(holder.binding) {
            landDetailPageTextview.text = "${position + 1} / ${imageUrlList.size}"
            Glide.with(context).load(imageUrlList[position]).apply(
                RequestOptions()
                    .fitCenter()
                    .override(LAND.LAND_DETAIL_IMAGE_VIEW_WIDTH, LAND.LAND_DETAIL_IMAGE_VIEW_HEIGHT)
            ).into(landDetailLandImageview)
        }
    }

    override fun getItemCount() = imageUrlList.size

    fun setData(urlList: List<String>) {
        imageUrlList = urlList
        notifyDataSetChanged()
    }
}