package `in`.koreatech.koin.ui.businessmain.adapter

import `in`.koreatech.koin.databinding.BusinessMenuItemBinding
import `in`.koreatech.koin.ui.businessmain.MenuItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BusinessMenuAdapter(
    private var menuList: MutableList<MenuItem>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<BusinessMenuAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: BusinessMenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            with(binding) {
                textViewMenuItem.text = item.title
                imageViewMenuItem.setImageResource(item.imageResource)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BusinessMenuItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuList[position])
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    fun setMenuList(newMenuList: List<MenuItem>) {
        menuList.clear()
        menuList.addAll(newMenuList)
        notifyDataSetChanged()
    }
}