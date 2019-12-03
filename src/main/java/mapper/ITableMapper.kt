package mapper

import entity.Table
import entity.TableFields

interface ITableMapper {

    fun getAllTables(): List<Table>

    fun getTablesByName(): List<Table>

    fun getTableFields(tableName: String): List<TableFields>
}