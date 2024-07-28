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
    private val textWatcherMap: ArrayList<TextWatcher> = ArrayList()

    inner class MenuViewHolder(val binding: ItemReviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.deleteMenuButton.setOnClickListener {
                textWatcherMap.removeAt(position)
                menuList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                textWatcherMap.forEach {
                    binding.menuNameEditText.addTextChangedListener(it)
                }
            }
            val textWatcher = @SuppressLint("RestrictedApi")
            object : TextWatcherAdapter() {
                @SuppressLint("RestrictedApi")
                override fun afterTextChanged(s: Editable) {
                    if(position<menuList.size)
                        menuList[position] = s.toString()
                }
            }
            textWatcherMap.add(textWatcher)
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
        notifyItemInserted(menuList.size)
    }

    fun getMenuList(): ArrayList<String> {
        return menuList
    }

}