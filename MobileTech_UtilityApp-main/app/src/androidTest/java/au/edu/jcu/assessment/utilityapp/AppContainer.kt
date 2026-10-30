package au.edu.jcu.assessment.utilityapp

import au.edu.jcu.assessment.utilityapp.data.AdviceApi
import au.edu.jcu.assessment.utilityapp.data.AdviceRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.adviceslip.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val adviceApi = retrofit.create(AdviceApi::class.java)

    val adviceRepository = AdviceRepository(adviceApi)
}