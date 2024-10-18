
data class CompleteInventoryResponse (
    val message: String,
    val missing_items: List<String>,
    val extra_items: List<String>
)


