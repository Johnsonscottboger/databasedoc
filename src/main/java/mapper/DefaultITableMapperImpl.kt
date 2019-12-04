package mapper

import entity.Table
import entity.TableFields

class DefaultITableMapperImpl : ITableMapper {
    override fun getAllTables(): List<Table> {
        return listOf(Table("table1", "表1"), Table("table2", "表2"))
    }

    override fun getTablesByName(name: String): List<Table> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTableFields(tableName: String): List<TableFields> {
        return listOf(TableFields("id", "VARCHAR", "NO", "YES", "主键", ""),
                TableFields("name", "VARCHAR", "YES", "", "名称", ""))
    }
}