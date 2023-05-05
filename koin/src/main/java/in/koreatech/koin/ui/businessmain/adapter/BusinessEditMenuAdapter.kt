package `in`.koreatech.koin.ui.businessmain.adapter

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.EditBusinessMenuItemBinding
import `in`.koreatech.koin.ui.businessmain.MenuItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BusinessEditMenuAdapter(
    private var menuList: MutableList<MenuItem>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<BusinessEditMenuAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: EditBusinessMenuItemBinding
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

        fun bind(item: MenuItem) {
            with(binding) {
                textViewMenuItem.text = item.title
                imageViewMenuItem.setImageResource(item.imageResource)
                constraintLayoutMenu.background = if (item.isSelected) {
                    itemView.context.getDrawable(R.drawable.bg_f7941e_rectangle)
                } else {
                    itemView.context.getDrawable(R.drawable.bg_dedede_rectangle)
                }
                imageViewCircle.setImageResource(
                    if (item.isSelected) R.drawable.ic_circle_minus
                    else R.drawable.ic_circle_plus
                )
            }
        }

        private fun toggleBackground(item: MenuItem) {
            item.isSelected = !item.isSelected

            if (item.isSelected) {
                with(binding) {
                    constraintLayoutMenu.background = itemView.context.getDrawable(R.drawable.bg_f7941e_rectangle)
                    imageViewCircle.setImageResource(R.drawable.ic_circle_minus)
                }
            } else {
                with(binding) {
                    binding.constraintLayoutMenu.background = itemView.context.getDrawable(R.drawable.bg_dedede_rectangle)
                    imageViewCircle.setImageResource(R.drawable.ic_circle_plus)
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

    fun setMenuList(newMenuList: List<MenuItem>) {
        menuList = newMenuList.toMutableList()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<MenuItem> {
        return menuList.filter { it.isSelected }
    }

}
