package `in`.koreatech.koin.ui.business.insertmemu.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityInsertMemuBinding
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuCategory
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuInfo
import `in`.koreatech.koin.ui.business.insertmemu.adapter.CombineViewModel
import `in`.koreatech.koin.ui.business.insertmemu.adapter.InsertMenuAdapter
import `in`.koreatech.koin.ui.business.insertmemu.dialog.InsertMenuBottomSheet
import `in`.koreatech.koin.ui.business.insertmemu.dialog.ReviseMenuBottomSheet
import `in`.koreatech.koin.ui.business.insertmemu.viewmodel.InsertMenuViewModel
import `in`.koreatech.koin.ui.business.registerstore.fragment.RegisterStoreCategoryFragment

class InsertMenuActivity: ActivityBase() {
    private val binding by dataBinding<ActivityInsertMemuBinding>(R.layout.activity_insert_memu)
    private val viewModel by viewModels<InsertMenuViewModel>()
    private val myAdapter = InsertMenuAdapter()

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

        initImage()
        setView()
        deleteImage()
        menuCategorySelector()

        binding.increasePriceButton.setOnClickListener {
            viewModel.addData(MenuInfo("tmp", "tmp"))
        }

        binding.confirmButton.setOnClickListener{
            myAdapter.initDataInViewModel(viewModel)
            viewModel.getMenuNameCombo(binding.initMenuName.text.toString(), binding.initMenuCombo.text.toString())
            startActivity(Intent(this@InsertMenuActivity, InsertMenuFinalActivity::class.java))
        }

        myAdapter.setItemClickListener(object: InsertMenuAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                viewModel.removeData(position)
            }
        })

        val combineViewModel = CombineViewModel
        combineViewModel.viewModel = viewModel

        viewModel.menuDataList.observe(this, Observer {
            myAdapter.setData(it)
        })

        viewModel.uriList.observe(this, Observer {
            if (it.size == 0){
                binding.firstMenuImage.setImageResource(0)
                binding.secondMenuImage.visibility = View.GONE
                binding.deleteFirstMenuImage.visibility = View.GONE
            }
            else if(it.size == 1){
                binding.firstMenuImage.setImageURI(it[0])

                binding.deleteFirstMenuImage.visibility = View.VISIBLE
                binding.secondMenuImage.visibility = View.VISIBLE
                binding.secondMenuImage.setImageResource(0)

                binding.thirdMenuImage.visibility = View.GONE
                binding.deleteSecondMenuImage.visibility = View.GONE
            }
            else if(it.size == 2){
                binding.firstMenuImage.setImageURI(it[0])
                binding.secondMenuImage.setImageURI(it[1])

                binding.deleteSecondMenuImage.visibility = View.VISIBLE
                binding.thirdMenuImage.visibility = View.VISIBLE
                binding.thirdMenuImage.setImageResource(0)

                binding.deleteThirdMenuImage.visibility = View.GONE
            }
            else if(it.size == 3){
                binding.firstMenuImage.setImageURI(it[0])
                binding.secondMenuImage.setImageURI(it[1])
                binding.thirdMenuImage.setImageURI(it[2])
                binding.deleteThirdMenuImage.visibility = View.VISIBLE
            }
        }) // Uri 리스트 확인해서 이미지뷰를 보여줄지 결정
    }

    private fun modalBottomSheet() {
        val modal = InsertMenuBottomSheet()
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(supportFragmentManager, modal.tag)
    }

    private fun reviseModalBottomSheet(position: Int) {
        val modal = ReviseMenuBottomSheet(position)
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(supportFragmentManager, modal.tag)
    }

    private fun initImage(){
        binding.firstMenuImage.setOnClickListener {
            //modalBottomSheet()
            if(binding.firstMenuImage.drawable == null){
                modalBottomSheet()
            }
            else {
                reviseModalBottomSheet(0)
            }
        }
        binding.secondMenuImage.setOnClickListener {
            if(binding.secondMenuImage.drawable == null){
                modalBottomSheet()
            }
            else {
                reviseModalBottomSheet(1)
            }
        }
        binding.thirdMenuImage.setOnClickListener {
            if(binding.thirdMenuImage.drawable == null){
                modalBottomSheet()
            }
            else {
                reviseModalBottomSheet(2)
            }
        }
    }

    private fun deleteImage(){
        binding.deleteFirstMenuImage.setOnClickListener {
            viewModel.deleteUri(0)
        }

        binding.deleteSecondMenuImage.setOnClickListener {
            viewModel.deleteUri(1)
        }

        binding.deleteThirdMenuImage.setOnClickListener {
            viewModel.deleteUri(2)
        }
    }

    private fun setView(){
        binding.secondMenuImage.visibility = View.GONE
        binding.thirdMenuImage.visibility = View.GONE
        binding.deleteFirstMenuImage.visibility = View.GONE
        binding.deleteSecondMenuImage.visibility = View.GONE
        binding.deleteThirdMenuImage.visibility = View.GONE
    }

    private fun menuCategorySelector(){
        binding.eventMenuBtn.setOnClickListener {
            binding.eventMenuBtn.isSelected = !binding.eventMenuBtn.isSelected
            MenuCategory.Option1.isCheck = binding.eventMenuBtn.isSelected

        }

        binding.representativeMenuBtn.setOnClickListener {
            binding.representativeMenuBtn.isSelected = !binding.representativeMenuBtn.isSelected
            MenuCategory.Option2.isCheck = binding.representativeMenuBtn.isSelected

        }

        binding.sideMenuBtn.setOnClickListener {
            binding.sideMenuBtn.isSelected = !binding.sideMenuBtn.isSelected
            MenuCategory.Option3.isCheck = binding.sideMenuBtn.isSelected

        }

        binding.setMenuBtn.setOnClickListener {
            binding.setMenuBtn.isSelected = !binding.setMenuBtn.isSelected
            MenuCategory.Option4.isCheck = binding.setMenuBtn.isSelected

        }
    }

}