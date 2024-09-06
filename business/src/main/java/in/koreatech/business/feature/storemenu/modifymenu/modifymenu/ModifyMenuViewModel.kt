package `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.constant.STORE_MENU_IMAGE_MAX
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.business.menu.GetMenuCategoryUseCase
import `in`.koreatech.koin.domain.usecase.business.menu.GetMenuInfoUseCase
import `in`.koreatech.koin.domain.usecase.business.menu.ModifyMenuUseCase
import `in`.koreatech.koin.domain.usecase.business.menu.RegisterMenuUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ModifyMenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuCategoryUseCase : GetMenuCategoryUseCase,
    private val getMarketPreSignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val getMenuInfoUseCase: GetMenuInfoUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val modifyMenuUseCase: ModifyMenuUseCase
): ViewModel(), ContainerHost<ModifyMenuState, ModifyMenuSideEffect> {
    override val container = container<ModifyMenuState, ModifyMenuSideEffect>(ModifyMenuState())

    private val menuId: Int = checkNotNull(savedStateHandle["menuId"])
    init {
        settingId(menuId)
    }

    private fun getStoreMenuInfo(){
        intent {
            viewModelScope.launch {
                val menuInfo = getMenuInfoUseCase(state.menuId)
                reduce {
                    state.copy(
                        shopId = menuInfo.shopId,
                        menuName = menuInfo.name,
                        menuPrice = menuInfo.singlePrice.toString(),
                        menuOptionPrice = menuInfo.optionPrice,
                        description = menuInfo.description,
                        menuCategoryId = menuInfo.categoryIds,
                        imageUriList = menuInfo.imageUrl
                    )
                }
                getStoreMenuCategory()
                addDefaultImage()
            }
        }
    }

    private fun getStoreMenuCategory(){
        intent {
            viewModelScope.launch {
                val menuCategory = getMenuCategoryUseCase(state.shopId)
                val newMenuCategory = menuCategory.map { category ->
                    StoreMenuCategory(
                        menuCategoryId = category.menuCategoryId,
                        menuCategoryName = category.menuCategoryName,
                        menuCategoryIsChecked = category.menuCategoryId in state.menuCategoryId
                    )
                }

                reduce {
                    state.copy(
                        menuCategory = newMenuCategory
                    )
                }
            }
        }
    }

    private fun addDefaultImage(){
        intent{
            reduce {
                val newMenuUriList = state.imageUriList.toMutableList()

                if(newMenuUriList.size != STORE_MENU_IMAGE_MAX){
                    newMenuUriList.add(ImageHolder.TempUri.toString())
                }
                state.copy(
                    imageUriList = newMenuUriList
                )
            }
        }
    }

    private fun makeMenuCategoryString(){
        intent {

            val menuLabels = mutableListOf<String>()
            val menuCategoryId = mutableListOf<Int>()

            state.menuCategory.forEach{
                if(it.menuCategoryIsChecked){
                    menuLabels.add(it.menuCategoryName)
                    menuCategoryId.add(it.menuCategoryId)
                }
            }
            reduce {
                state.copy(
                    menuCategoryLabel = menuLabels.joinToString(separator = " / "),
                    menuCategoryId = menuCategoryId
                )
            }
        }
    }

    private fun insertStoreFileUrl(url: String) {
        intent{
            val newImageUrl = state.imageUrlList.toMutableList()
            newImageUrl.add(url)

            reduce {
                state.copy(
                    imageUrlList = newImageUrl
                )
            }

            if((state.imageUriList.last() == TEMP_IMAGE_URI && (state.imageUrlList.size == state.imageUriList.size - 1)) || state.imageUrlList.size == STORE_MENU_IMAGE_MAX){
                modifyMenu()
            }
        }
    }

    private fun modifyMenu(){
        intent {
            viewModelScope.launch{
                    modifyMenuUseCase(
                        menuId = state.menuId,
                        menuCategoryId = state.menuCategoryId,
                        description = state.description,
                        menuImageUrlList = state.imageUrlList,
                        menuName = state.menuName,
                        menuOptionPrice = state.menuOptionPrice,
                        menuSinglePrice = state.menuPrice
                    ).onSuccess {
                        postSideEffect(ModifyMenuSideEffect.FinishModifyMenu)
                        reduce {
                            state.copy(
                                imageUrlList = emptyList()
                            )
                        }
                    }.onFailure {
                        postSideEffect(ModifyMenuSideEffect.ShowMessage(ModifyMenuErrorType.FailModifyMenu))
                        reduce {
                            state.copy(
                                imageUrlList = emptyList()
                            )
                        }
                    }
            }
        }
    }

    private fun failUploadImage() = intent{
        postSideEffect(ModifyMenuSideEffect.ShowMessage(ModifyMenuErrorType.FailUploadImage))
    }

    private fun settingId(menuId: Int){
        intent{
            if(menuId != state.menuId){
                reduce {
                    state.copy(
                        menuId = menuId
                    )
                }
                getStoreMenuInfo()

            }
        }
    }

    fun changeMenuImageUri(uriList: List<Uri>){
        intent {
            reduce {
                if(uriList.size < STORE_MENU_IMAGE_MAX){
                    val newMenuUriList = state.imageUriList.toMutableList()

                    newMenuUriList.removeAt(newMenuUriList.lastIndex)

                    for(imageUri in uriList)
                        newMenuUriList.add(imageUri.toString())

                    if(newMenuUriList.size != STORE_MENU_IMAGE_MAX)newMenuUriList.add(ImageHolder.TempUri.toString())

                    state.copy(
                        imageUriList = newMenuUriList
                    )
                }
                else{
                    state.copy(
                        imageUriList = uriList.toStringList()
                    )
                }
            }
        }
    }

    fun deleteMenuImageUri(index: Int) {
        intent {
            val newMenuUriList = state.imageUriList.toMutableList()
            newMenuUriList.removeAt(index)
            if(newMenuUriList.last() != TEMP_IMAGE_URI) {
                newMenuUriList.add(ImageHolder.TempUri.toString())
            }

            reduce {
                    state.copy(
                        imageUriList = newMenuUriList
                    )
            }
        }
    }

    fun modifyMenuImageUri(modifyUri: String) {
        intent {
            val newMenuUriList = state.imageUriList.toMutableList()
            newMenuUriList[state.imageIndex] = modifyUri
            reduce {
                state.copy(
                    imageUriList = newMenuUriList
                )
            }
        }
    }

    fun changeMenuName(menuName: String) = intent{
        reduce {
            state.copy(
                menuName = menuName
            )
        }
    }

    fun changeMenuPrice(price: String){
        intent{
            reduce {
                state.copy(menuPrice = price)
            }
        }
    }

    fun changeDetailMenuServing(index: Int, serving: String){
        intent{
            if (index in state.menuOptionPrice.indices) {
                reduce {
                    val newMenuPrice = state.menuOptionPrice.toMutableList()
                    newMenuPrice[index] = StoreMenuOptionPrice(PriceHolder.PriceString(serving).priceString, newMenuPrice[index].price)
                    state.copy(menuOptionPrice = newMenuPrice)
                }
            }
        }
    }

    fun changeDetailMenuPrice(index: Int, price: String){
        intent{
            if (index in state.menuOptionPrice.indices) {
                reduce {
                    val newMenuPrice = state.menuOptionPrice.toMutableList()
                    newMenuPrice[index] = StoreMenuOptionPrice(newMenuPrice[index].option, PriceHolder.PriceString(
                        price
                    ).priceString)
                    state.copy(menuOptionPrice = newMenuPrice)
                }
            }
        }
    }

    fun addPrice(){
        intent{
            reduce {
                val newMenuPrice = state.menuOptionPrice.toMutableList()
                newMenuPrice.add(StoreMenuOptionPrice(PriceHolder.TempPrice.toString(), PriceHolder.TempPrice.toString()))
                state.copy(
                    menuOptionPrice = newMenuPrice,
                    menuPrice = ""
                )
            }
        }
    }

    fun deleteMenuPrice(index: Int){
        intent {
            val newMenuPrice = state.menuOptionPrice.toMutableList()
            newMenuPrice.removeAt(index)
            reduce {
                state.copy(
                    menuOptionPrice = newMenuPrice
                )
            }
        }
    }

    fun menuCategoryIsClicked(index: Int){
        intent{
            reduce {
                val newMenuCategory = state.menuCategory.toMutableList()
                newMenuCategory[index] = StoreMenuCategory(newMenuCategory[index].menuCategoryId, newMenuCategory[index].menuCategoryName, !newMenuCategory[index].menuCategoryIsChecked)

                state.copy(
                    menuCategory = newMenuCategory,
                )
            }
        }
    }

    fun isImageModify(isModify: Boolean)=intent{
        reduce {
            state.copy(
                isModify = isModify
            )
        }
    }

    fun setImageIndex(index: Int)= intent{
        reduce {
            state.copy(
                imageIndex = index
            )
        }
    }

    fun changeMenuDetail(menuDetail: String) = intent{
        reduce {
            state.copy(
                description = menuDetail
            )
        }
    }

    fun onNextButtonClick(){
        intent{
            when {
                state.menuName.isBlank()-> postSideEffect(
                    ModifyMenuSideEffect.ShowMessage(
                        ModifyMenuErrorType.NullMenuName
                    )
                )
                state.menuPrice.isBlank() && state.menuOptionPrice.isEmpty()-> postSideEffect(
                    ModifyMenuSideEffect.ShowMessage(ModifyMenuErrorType.NullMenuPrice)
                )
                !state.menuCategory.take(state.menuCategory.size).any { it.menuCategoryIsChecked } -> postSideEffect(
                    ModifyMenuSideEffect.ShowMessage(ModifyMenuErrorType.NullMenuCategory)
                )
                state.description.isBlank() -> postSideEffect(
                    ModifyMenuSideEffect.ShowMessage(
                        ModifyMenuErrorType.NullMenuDescription
                    )
                )
                state.imageUriList.size == 1 && state.imageUriList[0] == TEMP_IMAGE_URI -> postSideEffect(
                    ModifyMenuSideEffect.ShowMessage(ModifyMenuErrorType.NullMenuImage)
                )
                else ->{
                    makeMenuCategoryString()
                    postSideEffect(ModifyMenuSideEffect.GoToCheckMenuScreen)
                }
            }
        }
    }

    fun onPositiveButtonClicked(context: Context){
        intent {

            state.imageUriList.forEach { uriString ->
                if (uriString != TEMP_IMAGE_URI)
                {
                    if(uriString.contains("content")){
                        val uri = Uri.parse(uriString)

                        val inputStream = context.contentResolver.openInputStream(uri)

                        if (uri.scheme.equals("content")) {
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor.use {
                                if (cursor != null && cursor.moveToFirst()) {
                                    val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                    val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                                    if (fileNameIndex != -1 && fileSizeIndex != -1) {
                                        val fileName = cursor.getString(fileNameIndex)
                                        val fileSize = cursor.getLong(fileSizeIndex)

                                        if (inputStream != null) {
                                            getPreSignedUrl(
                                                fileSize = fileSize,
                                                fileType = "image/" + fileName.split(".")[1],
                                                fileName = fileName,
                                                imageUri = uriString
                                            )
                                        }
                                        inputStream?.close()
                                    }
                                }
                            }
                        }
                    }
                    else{
                        insertStoreFileUrl(uriString)
                    }
                }
            }
        }
    }

    private fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String
    ) {
        intent {
                viewModelScope.launch{
                    getMarketPreSignedUrlUseCase(
                        fileSize, fileType, fileName
                    ).onSuccess {
                        uploadImage(
                            preSignedUrl = it.second,
                            fileUrl = it.first,
                            mediaType = fileType,
                            mediaSize = fileSize,
                            imageUri = imageUri
                        )
                    }.onFailure {
                        failUploadImage()
                    }
                }
            }
        }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: String
    ) {
        viewModelScope.launch{
            uploadFilesUseCase(
                preSignedUrl,
                mediaType,
                mediaSize,
                imageUri
            ).onSuccess {
                insertStoreFileUrl(fileUrl)
            }.onFailure {
                failUploadImage()
            }
        }
    }

}


fun List<Uri>.toStringList(): List<String> {
    val responseList = ArrayList<String>()

    for (uri in this){
        responseList.add(uri.toString())
    }

    return responseList
}
const val TEMP_IMAGE_URI = "TempUri"
