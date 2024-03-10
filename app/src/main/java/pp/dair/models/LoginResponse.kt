package pp.dair.models

data class LoginResponse(
    val location: String?,
    val message: String,
    val tip: String,
    val success: Int,
    val teacher_id: Int?
)
