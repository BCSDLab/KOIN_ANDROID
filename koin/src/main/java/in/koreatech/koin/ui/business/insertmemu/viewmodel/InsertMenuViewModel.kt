package `in`.koreatech.koin.ui.business.insertmemu.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuCategory
import `in`.koreatech.koin.domain.model.business.insertMenu.MenuInfo
import javax.inject.Inject

class InsertMenuViewModel  @Inject constructor(): BaseViewModel(){
    private val _menuDataList = MutableLiveData<ArrayList<MenuInfo>>()
    val menuDataList: LiveData<ArrayList<MenuInfo>> get() = _menuDataList
    //메뉴 데이터

    private val _uriList = MutableLiveData<ArrayList<Uri>>()
    val uriList: LiveData<ArrayList<Uri>> get() = _uriList
    // 이미지 Uri

    private val _menuName = MutableLiveData<String>()
    val menuName: LiveData<String> get() = _menuName

    private val _menuCombo = MutableLiveData<String>()
    val menuCombo: LiveData<String> get() = _menuCombo

    private var menuItems = ArrayList<MenuInfo>()
    private var imageItems = ArrayList<Uri>()

    init{
        _menuDataList.value = menuItems
        _uriList.value = imageItems
    }

    fun addData(menuInfo: MenuInfo)
    {
        menuItems.add(menuInfo)
        _menuDataList.value = menuItems
    }

    fun removeData(position: Int) {
        menuItems.removeAt(position)
        _menuDataList.value = menuItems
    }

    fun setUri(uri: Uri){
        imageItems.add(uri)
        _uriList.value = imageItems
        Log.d("뷰모델", imageItems.toString())
    }

    fun deleteUri(position: Int){
        imageItems.removeAt(position)
        _uriList.value = imageItems
    }

    fun reviseUri(uri: Uri, position: Int){
        imageItems[position] = uri
        _uriList.value = imageItems
        Log.d("뷰모델", imageItems.toString())
    }

    fun getData(data: ArrayList<MenuInfo>){
        menuItems = data
        _menuDataList.value = menuItems
    }

    fun getMenuCategory() :String{
        var menuCategory: String = "null"
        if(MenuCategory.Option1.isCheck)
            if (menuCategory == "null") menuCategory = MenuCategory.Option1.option

        if(MenuCategory.Option2.isCheck){
            if (menuCategory == "null") menuCategory = MenuCategory.Option2.option
            else menuCategory += (" / " + MenuCategory.Option2.option)
        }

        if(MenuCategory.Option3.isCheck){
            if (menuCategory == "null") menuCategory = MenuCategory.Option3.option
            else menuCategory += (" / " + MenuCategory.Option3.option)
        }

        if(MenuCategory.Option4.isCheck){
            if (menuCategory == "null") menuCategory = MenuCategory.Option4.option
            else menuCategory += (" / " + MenuCategory.Option4.option)
        }

        return menuCategory
    }

    fun getMenuNameCombo(name: String, combo: String){
        _menuName.value = name
        _menuCombo.value = combo
    }
}