import entity.Table
import entity.TableFields
import mapper.DefaultITableMapperImpl
import mapper.ITableMapper
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import utils.TableToWordUtil

fun main(args: Array<String>) {
    println("*****欢迎使用!*****")


//    val factory = getSessionFactory()
//    val mapper = getMapper(factory)
//    val tables = mapper.getAllTables()

    val mapper = DefaultITableMapperImpl()
    println("正在查找表...")
    val tables = mapper.getAllTables()
    printGreen("共查找到${tables.size}个表")

    TableToWordUtil(mapper).toWord(tables, "数据库文档.doc", "数据库文档")
}

fun getSessionFactory(): SqlSessionFactory? {
    val resource = "configuration.xml"
    println("正在初始化数据库连接...")
    return try {
        SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource))
    } catch (ex: Exception) {
        printRed("初始化数据库失败")
        null
    }
}

fun getMapper(factory: SqlSessionFactory): ITableMapper {
    println("正在打开数据库连接...")
    val session = factory.openSession()
    printGreen("打开数据库成功: ${session.configuration.databaseId}")
    return session.getMapper(ITableMapper::class.java)
}

fun printGreen(message: String) {
    println("\\033[47;32m$message\\033[5m")
}

fun printRed(message: String) {
    println("\\033[47;31m$message\\033[5m")
}