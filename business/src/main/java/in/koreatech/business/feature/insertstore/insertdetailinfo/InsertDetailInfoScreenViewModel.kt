package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InsertDetailInfoScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel(), ContainerHost<InsertDetailInfoScreenState, InsertDetailInfoScreenSideEffect> {
        override val container: Container<InsertDetailInfoScreenState, InsertDetailInfoScreenSideEffect> =
            container(InsertDetailInfoScreenState(), savedStateHandle = savedStateHandle) {

                val storeBasicInfoJson: InsertBasicInfoScreenState? = savedStateHandle.get<InsertBasicInfoScreenState>("storeBasicInfo")
                if (storeBasicInfoJson != null) getStoreBasicInfo(storeBasicInfoJson)
            }

        fun getStoreBasicInfo(storeBasicInfo: InsertBasicInfoScreenState?) {
            intent {
                if (storeBasicInfo != null) {
                    reduce {
                        state.copy(
                            storeCategory = storeBasicInfo.storeCategory,
                            storeName = storeBasicInfo.storeName,
                            storeAddress = storeBasicInfo.storeAddress,
                            storeImage = storeBasicInfo.storeImageFileUrl,
                        )
                    }
                }
            }
        }

        private fun isDetailInfoValid() =
            intent {
                reduce {
                    state.copy(
                        isDetailInfoValid = state.storePhoneNumber.isNotEmpty() && state.storeDeliveryFee.isNotEmpty() && state.storeOtherInfo.isNotEmpty(),
                    )
                }
            }

        fun onChangeSettingTimeList(timeList: List<TimeSettingState>) =
            intent {
                val operatingTimeList: MutableList<OperatingTimeState> = mutableListOf()

                timeList.forEach { timeInfo ->
                    if (timeInfo.isClosed) {
                        timeInfo.dayOfWeekList.forEach { day ->
                            operatingTimeList.add(
                                OperatingTimeState(
                                    closeTime = "00:00",
                                    closed = true,
                                    dayOfWeek = day.kor,
                                    openTime = "00:00",
                                    dayOfWeekEnglish = day.eng,
                                    priority = day.priority,
                                ),
                            )
                        }
                    } else if (timeInfo.is24Hours) {
                        timeInfo.dayOfWeekList.forEach { day ->
                            operatingTimeList.add(
                                OperatingTimeState(
                                    closeTime = "23:59",
                                    closed = false,
                                    dayOfWeek = day.kor,
                                    openTime = "00:00",
                                    dayOfWeekEnglish = day.eng,
                                    priority = day.priority,
                                ),
                            )
                        }
                    } else {
                        timeInfo.dayOfWeekList.forEach { day ->
                            operatingTimeList.add(
                                OperatingTimeState(
                                    closeTime = timeInfo.closeTime,
                                    closed = false,
                                    dayOfWeek = day.kor,
                                    openTime = timeInfo.openTime,
                                    dayOfWeekEnglish = day.eng,
                                    priority = day.priority,
                                ),
                            )
                        }
                    }
                }
                reduce {
                    state.copy(
                        operatingTimeList = operatingTimeList.sortedBy { it.priority },
                        settingTimeInfoList = timeList,
                    )
                }
            }

        fun onChangePhoneNumber(phoneNumber: String) =
            blockingIntent {
                reduce {
                    state.copy(storePhoneNumber = phoneNumber)
                }
                isDetailInfoValid()
            }

        fun onChangeDeliveryFee(deliveryFee: String) =
            blockingIntent {
                reduce {
                    state.copy(storeDeliveryFee = deliveryFee)
                }
                isDetailInfoValid()
            }

        fun onChangeOtherInfo(otherInfo: String) =
            blockingIntent {
                reduce {
                    state.copy(storeOtherInfo = otherInfo)
                }
                isDetailInfoValid()
            }

        fun changeIsDeliveryOk() =
            intent {
                reduce {
                    state.copy(isDeliveryOk = !state.isDeliveryOk)
                }
            }

        fun changeIsCardOk() =
            intent {
                reduce {
                    state.copy(isCardOk = !state.isCardOk)
                }
            }

        fun changeIsBankOk() =
            intent {
                reduce {
                    state.copy(isBankOk = !state.isBankOk)
                }
            }

        fun onNextButtonClick() {
            intent {
                if (state.isDetailInfoValid) {
                    val storeDetailInfo = state
                    postSideEffect(InsertDetailInfoScreenSideEffect.NavigateToCheckScreen(storeDetailInfo))
                    return@intent
                }

                when {
                    state.storePhoneNumber.isEmpty() ->
                        postSideEffect(
                            InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStorePhoneNumber),
                        )
                    state.storeDeliveryFee.isEmpty() ->
                        postSideEffect(
                            InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStoreDeliveryFee),
                        )
                    state.storeOtherInfo.isEmpty() ->
                        postSideEffect(
                            InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStoreOtherInfo),
                        )
                }
            }
        }
    }
