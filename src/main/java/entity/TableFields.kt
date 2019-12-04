package entity

data class TableFields(
        val field: String,
        val type: String,
        val length: String,
        val nullable: String,
        val comment: String,
        val default: String
)