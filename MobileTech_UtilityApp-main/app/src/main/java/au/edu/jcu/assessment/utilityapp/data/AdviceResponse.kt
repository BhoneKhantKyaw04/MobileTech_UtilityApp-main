package au.edu.jcu.assessment.utilityapp.data

data class AdviceResponse(val slip: Slip)

data class Slip(val id: Int, val advice: String)