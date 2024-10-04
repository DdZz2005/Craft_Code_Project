data class ItemResponse(
    val id: Int,
    val name:String,
    val description: String,
    val serial_number: String,
    val warehouse: Int,
    var location: String,
    var assigned_to: String,
    var registration_date: String
)
