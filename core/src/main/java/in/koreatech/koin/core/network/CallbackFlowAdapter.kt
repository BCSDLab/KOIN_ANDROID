package `in`.koreatech.koin.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CallbackFlowAdapter<T>(
    private val successBodyType: Type
) : CallAdapter<T, Flow<T>> {
    override fun responseType(): Type = successBodyType

    override fun adapt(call: Call<T>): Flow<T> = callbackFlow {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful) {
                    response.body()?.let { trySend(it) } ?: close(
                        ResponseEmptyBodyException()
                    )
                } else {
                    close(NetworkException(response.code()))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                close(t)
            }
        })
    }

    class Factory : CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? {

            if (Call::class.java != getRawType(returnType)) {
                return null
            }

            check(returnType is ParameterizedType) {
                "return type must be parameterized as Flow<T> or Flow<out T>"
            }

            val responseType = getParameterUpperBound(0, returnType)

            if (getRawType(responseType) != Result::class.java) {
                return null
            }

            check(responseType is ParameterizedType) {
                "Response must be parameterized as Result<Foo> or Result<out Foo>"
            }

            val successBodyType = getParameterUpperBound(0, responseType)

            return CallbackFlowAdapter<Any>(successBodyType)
        }
    }


}