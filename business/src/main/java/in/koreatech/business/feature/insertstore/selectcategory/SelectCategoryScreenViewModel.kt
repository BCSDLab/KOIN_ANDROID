package `in`.koreatech.business.feature.insertstore.selectcategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.OwnerChangePasswordUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class SelectCategoryScreenViewModel @Inject constructor(
    private val ownerChangePasswordUseCase: OwnerChangePasswordUseCase
) : ViewModel(), ContainerHost<SelectCategoryScreenState, SelectCategoryScreenSideEffect>{
    override val container = container<SelectCategoryScreenState,SelectCategoryScreenSideEffect>(SelectCategoryScreenState())







}