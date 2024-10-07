data class CompanyModel(
    val name: String = "Не указано",           // Название компании
    val adminFamily: String = "Не указано",    // Фамилия администратора
    val adminName: String = "Не указано",      // Имя администратора
    val adminPatronymic: String? = null        // Отчество администратора (может быть null)
)
