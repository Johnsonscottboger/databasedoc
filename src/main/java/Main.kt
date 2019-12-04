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


    val factory = getSessionFactory()
    val mapper = getMapper(factory)
    val tables = mapper.getAllTables()

    TableToWordUtil(mapper).toWord(tables, "测试数据库.doc", "数据库文档")
}

fun getSessionFactory(): SqlSessionFactory {
    val resource = "configuration.xml"
    return SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource))
}

fun getMapper(factory: SqlSessionFactory): ITableMapper {
    val session = factory.openSession()
    return session.getMapper(ITableMapper::class.java)
}