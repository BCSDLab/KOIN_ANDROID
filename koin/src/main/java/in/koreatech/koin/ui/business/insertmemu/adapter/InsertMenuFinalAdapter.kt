package `in`.koreatech.koin.ui.business.insertmemu.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.InsertMenuFinalItemBinding
import `in`.koreatech.koin.databinding.InsertMenuItemBinding
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuInfo
import `in`.koreatech.koin.ui.business.insertmemu.viewmodel.InsertMenuViewModel

class InsertMenuFinalAdapter : RecyclerView.Adapter<InsertMenuFinalAdapter.ViewHolder>() {
    var dataSet = ArrayList<MenuInfo>()
    //val viewModel: InsertMenuViewModel = viewModel

    class ViewHolder(private val binding: InsertMenuFinalItemBinding): RecyclerView.ViewHolder(binding.root){
        val menu = binding.menuServing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  InsertMenuFinalItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            menu.text = dataSet[position].serving + "\t/\t" + dataSet[position].price + "원"
        }
    }

    fun setData(data : ArrayList<MenuInfo>){
        dataSet = data
        Log.d("뷰모델이 다른가?",  data.toString())
        notifyDataSetChanged()
    }

}