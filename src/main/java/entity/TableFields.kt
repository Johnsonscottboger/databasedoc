package entity

data class TableFields(
        val tableName: String,
        val field: String,
        val type: String,
        val length: String?,
        val nullable: String?,
        val comment: String?
)