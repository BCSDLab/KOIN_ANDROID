package `in`.koreatech.koin.data.response.bus.express

import `in`.koreatech.koin.data.response.bus.BusTimetableResponse
import `in`.koreatech.koin.data.response.bus.shuttle.ShuttleBusTimetableResponse
import com.google.gson.annotations.SerializedName

data class ExpressBusTimetableResponse(
    @SerializedName("terminal_to_koreatech") val terminalToKoreatech: List<ExpressDepartureArrivalResponse>,
    @SerializedName("koreatech_to_terminal") val koreatechToTerminal: List<ExpressDepartureArrivalResponse>
)

val BusTimetableResponse.express: ExpressBusTimetableResponse
    get() = ExpressBusTimetableResponse(
    terminalToKoreatech = this.terminalToKoreatech ?: throw IllegalStateException("Not a express bus response"),
    koreatechToTerminal = this.koreatechToTerminal ?: throw IllegalStateException("Not a express bus response"),
)