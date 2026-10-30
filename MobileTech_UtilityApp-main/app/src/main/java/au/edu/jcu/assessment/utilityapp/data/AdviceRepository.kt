package au.edu.jcu.assessment.utilityapp.data

class AdviceRepository(private val api: AdviceApi) {

    suspend fun getAdvice(): Result<String> {
        return runCatching { api.getAdvice().slip.advice }
    }
}