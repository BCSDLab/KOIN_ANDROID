package `in`.koreatech.koin.ui.businessmain.adapter

import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.entity.MenuItemEntity
import `in`.koreatech.koin.databinding.EditBusinessMenuItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BusinessEditMenuAdapter(
    private var menuList: MutableList<MenuItemEntity>,
    private val onClick: (Int) -> Unit,
) : RecyclerView.Adapter<BusinessEditMenuAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: EditBusinessMenuItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleBackground(menuList[position])
                    onClick(position)
                }
            }
        }

        fun bind(item: MenuItemEntity) {
            with(binding) {
                textViewMenuItem.text = item.title
                Glide.with(itemView)
                    .load(item.imageResource)
                    .fitCenter()
                    .into(imageViewMenuItem)
                constraintLayoutMenu.background = if (item.isSelected) {
                    itemView.context.getDrawable(R.drawable.bg_f7941e_rectangle)
                } else {
                    itemView.context.getDrawable(R.drawable.bg_dedede_rectangle)
                }
                Glide.with(itemView)
                    .load(if (item.isSelected) R.drawable.ic_circle_minus else R.drawable.ic_circle_plus)
                    .fitCenter()
                    .into(imageViewCircle)
            }
        }

        private fun toggleBackground(item: MenuItemEntity) {
            item.isSelected = !item.isSelected

            if (item.isSelected) {
                with(binding) {
                    constraintLayoutMenu.background =
                        itemView.context.getDrawable(R.drawable.bg_f7941e_rectangle)
                    Glide.with(itemView)
                        .load(R.drawable.ic_circle_minus)
                        .fitCenter()
                        .into(imageViewCircle)
                }
            } else {
                with(binding) {
                    constraintLayoutMenu.background =
                        itemView.context.getDrawable(R.drawable.bg_dedede_rectangle)
                    Glide.with(itemView)
                        .load(R.drawable.ic_circle_plus)
                        .fitCenter()
                        .into(imageViewCircle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EditBusinessMenuItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    fun getSelectedItems(): List<MenuItemEntity> {
        return menuList.filter { it.isSelected }
    }

}
