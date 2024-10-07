data class InventoryRequest(
    val id: Int,
    val employee: String,
    val deadline: String,
    val warehouses: List<String>,
    val status: String
)
