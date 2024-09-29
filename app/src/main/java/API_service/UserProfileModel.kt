data class UserProfileModel(
    val first_name: String = "Не указано",         // Имя
    val last_name: String = "Не указано",         // Фамилия
    val username: String? = null,             // Отчество (может быть null)
    val email: String = "default@example.com",  // Электронная почта
    val password: String,                       // Пароль
    val is_superuser: String                           // Роль (например, "admin" или "employee")
)