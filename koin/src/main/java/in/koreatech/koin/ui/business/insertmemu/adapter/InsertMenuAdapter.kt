package `in`.koreatech.koin.ui.business.insertmemu.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.koreatech.koin.databinding.InsertMenuItemBinding
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuInfo
import `in`.koreatech.koin.ui.business.insertmemu.viewmodel.InsertMenuViewModel

class InsertMenuAdapter() : RecyclerView.Adapter<InsertMenuAdapter.ViewHolder>() {
    var dataSet = ArrayList<MenuInfo>()
    //val viewModel: InsertMenuViewModel = viewModel

    class ViewHolder(private val binding: InsertMenuItemBinding): RecyclerView.ViewHolder(binding.root){
        val menuPirce = binding.initMenuPrice
        val menuServing = binding.initMenuServing
        val deleteBtn = binding.deleteServingButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  InsertMenuItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  dataSet.size
    }

    fun setData(data : ArrayList<MenuInfo>){
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            deleteBtn.setOnClickListener {
                itemClickListener.onClick(it, position)
            }

            if(dataSet[position].price != "tmp") {
                menuPirce.setText(dataSet[position].price)
                menuServing.setText(dataSet[position].serving)
            }
            else{
                menuPirce.text = null
                menuServing.text = null
            }

            menuPirce.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(position != dataSet.size) {
                        dataSet[position].price = p0.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
            menuServing.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(position != dataSet.size) {
                        dataSet[position].serving = p0.toString()
                    }
                }
                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    fun initDataInViewModel(viewModel: InsertMenuViewModel){
        viewModel.getData(dataSet)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    } // 리사이클러뷰 아이템이 꼬이지 않도록 해준다.
}