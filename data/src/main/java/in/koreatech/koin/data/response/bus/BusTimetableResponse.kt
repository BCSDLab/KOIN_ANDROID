package `in`.koreatech.koin.data.response.bus

import `in`.koreatech.koin.data.response.bus.express.ExpressDepartureArrivalResponse
import `in`.koreatech.koin.data.response.bus.shuttle.ShuttleBusRouteResponse
import com.google.gson.annotations.SerializedName

data class BusTimetableResponse(
    //셔틀버스
    @SerializedName("to_school") val toSchool: List<ShuttleBusRouteResponse>?,
    @SerializedName("from_school") val fromSchool: List<ShuttleBusRouteResponse>?,

    //대성
    @SerializedName("terminal_to_koreatech") val terminalToKoreatech: List<ExpressDepartureArrivalResponse>?,
    @SerializedName("koreatech_to_terminal") val koreatechToTerminal: List<ExpressDepartureArrivalResponse>?
)
