data class UserProfileModel(
    val name: String = "Не указано",            // Имя
    val surname: String = "Не указано",         // Фамилия
    val patronymic: String? = null,             // Отчество (может быть null)
    val email: String = "default@example.com",  // Электронная почта
    val password: String,                       // Пароль
    val role: String                            // Роль (например, "admin" или "employee")
)