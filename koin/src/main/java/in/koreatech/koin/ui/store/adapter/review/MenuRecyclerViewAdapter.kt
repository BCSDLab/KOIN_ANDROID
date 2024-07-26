package `in`.koreatech.koin.ui.store.adapter.review

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.TextWatcherAdapter
import `in`.koreatech.koin.databinding.ItemReviewMenuBinding

class MenuRecyclerViewAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val menuList: ArrayList<String> = ArrayList()
    private val textWatcherMap: MutableMap<Int, TextWatcher> = mutableMapOf()

    inner class MenuViewHolder(val binding: ItemReviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.deleteMenuButton.setOnClickListener {
                menuList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
            binding.menuNameEditText.removeTextChangedListener(textWatcherMap[position])
            textWatcherMap[position] = @SuppressLint("RestrictedApi")
            object : TextWatcherAdapter() {
                @SuppressLint("RestrictedApi")
                override fun afterTextChanged(s: Editable) {
                    menuList[position] = s.toString()
                }
            }
            binding.menuNameEditText.addTextChangedListener(textWatcherMap[position])
            binding.menuNameEditText.setText(menuList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuViewHolder(
            ItemReviewMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return menuList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MenuViewHolder).bind(position)
    }

    fun addMenu() {
        menuList.add("")
        notifyItemInserted(menuList.size - 1)
    }

    fun getMenuList(): ArrayList<String> {
        return menuList
    }

}