package entity

data class TableFields(
        val field: String,
        val type: String,
        val allowNull: String,
        val isPrimaryKey: String,
        val comment: String,
        val default: String
)