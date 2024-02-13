package `in`.koreatech.koin.ui.business.insertmemu.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityInsertMenuFinalBinding
import `in`.koreatech.koin.ui.business.insertmemu.adapter.CombineViewModel.viewModel
import `in`.koreatech.koin.ui.business.insertmemu.adapter.InsertMenuFinalAdapter

class InsertMenuFinalActivity :ActivityBase(){
    private val binding by dataBinding<ActivityInsertMenuFinalBinding>(R.layout.activity_insert_menu_final)
    private val myAdapter = InsertMenuFinalAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.menuItemRecyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
            this.adapter = myAdapter
        }
        viewModel.menuDataList.value?.let { myAdapter.setData(it) }
        binding.menuCategoryTextview.text = viewModel.getMenuCategory()
        binding.menuNameTextview.text = viewModel.menuName.value
        binding.menuComboTextview.text = viewModel.menuCombo.value

        binding.cancleButton.setOnClickListener {
            finish()
        }

        viewModel.uriList.observe(this, Observer {
            when (it.size) {
                1 -> {
                    binding.firstMenuImage.setImageURI(it[0])
                }
                2 -> {
                    binding.firstMenuImage.setImageURI(it[0])
                    binding.secondMenuImage.setImageURI(it[1])
                }
                3 -> {
                    binding.firstMenuImage.setImageURI(it[0])
                    binding.secondMenuImage.setImageURI(it[1])
                    binding.thirdMenuImage.setImageURI(it[2])
                }
            }
        }) // Uri 리스트 확인해서 이미지뷰를 보여줄지 결정

    }
}