package au.edu.jcu.assessment.utilityapp.data

import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}