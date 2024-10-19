data class UserDetailsResponse(
    val surname: String,
    val name: String,
    val patronymic: String?,
    val email: String,
    val is_admin: Boolean,
    val is_employee: Boolean
)