import entity.Table
import entity.TableFields
import mapper.DefaultITableMapperImpl
import utils.TableToWordUtil

fun main(args: Array<String>) {
    println("Welcome")

    val tableMapperImpl = DefaultITableMapperImpl()

    TableToWordUtil(tableMapperImpl).toWord(tableMapperImpl.getAllTables(), "测试数据库.docx", "数据库文档")

}