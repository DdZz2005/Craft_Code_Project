data class UserProfileModel(
    val first_name: String = "Не указано",         // Имя
    val last_name: String = "Не указано",         // Фамилия
    val middle_name: String? = null,             // Отчество (может быть null)
    val email: String = "default@example.com",  // Электронная почта
    val phone_number: String,                    // Номер телефона
    val password: String,                       // Пароль
    val role: String                           // Роль (например, "admin" или "employee")
)