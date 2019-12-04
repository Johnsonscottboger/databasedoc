package utils

import com.lowagie.text.*
import com.lowagie.text.rtf.RtfWriter2
import config.Constants
import entity.Table
import entity.TableFields
import mapper.ITableMapper
import java.awt.Color
import java.io.File
import java.io.FileOutputStream

class TableToWordUtil(val tableMapper: ITableMapper) {

    fun toWord(tables: List<Table>, fileName: String, titleText: String) {
        val document = Document(PageSize.A4)
        val dir = File(Constants.FILE_PATH)
        val file = File(fileName)
        if (file.exists() && file.isFile)
            file.delete()
        file.createNewFile()

        RtfWriter2.getInstance(document, FileOutputStream(file))
        document.open()
        val ph = Paragraph()
        val f = Font()

        //标题
        val title = buildTitle(titleText)
        document.add(title)

        ph.font = f

        tables.forEachIndexed { index, table ->
            val tableName = table.name
            val comment = table.comment
            val fields = tableMapper.getTableFields(tableName)
            val head = buildHead(index, tableName, comment)
            val table = buildTable()
            fields.forEachIndexed { index, tableFields ->
                buildTableRow(table, index, tableFields)
            }
            document.add(head)
            document.add(table)
        }
        document.close()
    }

    private fun buildTitle(title: String): Paragraph {
        return Paragraph(title, Font(Font.NORMAL, 24F, Font.BOLDITALIC, Color.black)).apply {
            alignment = Element.ALIGN_CENTER
        }
    }

    private fun buildHead(index: Int, tableName: String, comment: String?): Paragraph {
        return Paragraph("${index.toString()}. $tableName($comment)")
    }

    private fun buildTable(): com.lowagie.text.Table {
        val table = com.lowagie.text.Table(6)
        buildTableHead(table)
        return table
    }

    private fun buildTableHead(table: com.lowagie.text.Table) {
        val color = Color(176, 196, 222)
        val buildCell: (String) -> Cell = { name ->
            Cell(name).apply { horizontalAlignment = Element.ALIGN_CENTER; backgroundColor = color }
        }
        table.addCell(buildCell("编号"))
        table.addCell(buildCell("字段"))
        table.addCell(buildCell("类型"))
        table.addCell(buildCell("是否可空"))
        table.addCell(buildCell("是否主键"))
        table.addCell(buildCell("说明"))
        table.endHeaders()
    }

    private fun buildTableRow(table: com.lowagie.text.Table, index: Int, tableFields: TableFields) {
        table.addCell(Cell((index + 1).toString()).apply { horizontalAlignment = Element.ALIGN_CENTER })
        table.addCell(Cell(tableFields.field))
        table.addCell(Cell(tableFields.type))
        table.addCell(Cell(if (tableFields.nullable == "YES") "Y" else "N").apply { horizontalAlignment = Element.ALIGN_CENTER })
        table.addCell(Cell(if (tableFields.default == "") "Y" else "N").apply { horizontalAlignment = Element.ALIGN_CENTER })
        table.addCell(Cell(tableFields.comment))
    }
}