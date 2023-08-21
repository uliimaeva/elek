package pp.dair.models

data class LoginPayload(
    val name: String,
    val password: String,
    val captcha: String
)
