package utils

import com.lowagie.text.*
import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.rtf.RtfWriter2
import com.lowagie.text.rtf.style.RtfParagraphStyle
import config.Constants
import entity.Table
import entity.TableFields
import mapper.ITableMapper
import java.awt.Color
import java.io.File
import java.io.FileOutputStream

class TableToWordUtil(val tableMapper: ITableMapper) {

    private val baseFont: BaseFont =
        BaseFont.createFont("C:\\Windows\\Fonts\\SIMSUN.TTC,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)


    fun toWord(tables: List<Table>, fileName: String, titleText: String) {
        val document = Document(PageSize.A4)
        val dir = File(Constants.FILE_PATH)
        val file = File(fileName)
        if (file.exists() && file.isFile)
            file.delete()
        file.createNewFile()

        RtfWriter2.getInstance(document, FileOutputStream(file))
        document.open()
        val title = buildTitle(titleText)
        document.add(title)
        tables.forEachIndexed { index, table ->
            val tableName = table.name
            val comment = table.comment
            println("正在处理第${index + 1}/${tables.size}个表")
            println("$tableName $comment")
            val fields = tableMapper.getTableFields(tableName)
            val head = buildHead(index, tableName)
            val t = buildTable(tableName, comment)
            fields.forEachIndexed { i, tableFields ->
                buildTableRow(t, index, tableFields)
            }
            document.add(head)
            document.add(Phrase(""))
            document.add(t)
            document.add(Paragraph(""))
        }
        document.close()
        println("处理完成!")
    }

    private fun buildTitle(title: String): Paragraph {
        return Paragraph(title, Font(Font.NORMAL, 24F, Font.BOLD, Color.black)).apply {
            alignment = Element.ALIGN_CENTER
        }
    }

    private fun buildHead(index: Int, tableName: String): Paragraph {
        return Paragraph("  $tableName", RtfParagraphStyle.STYLE_HEADING_3).apply {
            spacingBefore = 10F
            spacingAfter = 15F
        }
    }

    private fun buildTable(tableName: String, comment: String?): com.lowagie.text.Table {
        val widths = intArrayOf(30, 20, 10, 10, 30)
        val table = Table(5).apply {
            width = 95F;
            padding = 2F;
            setWidths(widths);
            setAutoFillEmptyCells(true)
        }
        buildTableHead(table, tableName, comment)
        return table
    }

    private fun buildTableHead(table: com.lowagie.text.Table, tableName: String, comment: String?) {
        val font = Font(baseFont, 12F)
        val buildCell: (String) -> Cell = { name ->
            Cell(Paragraph(name, font)).apply { horizontalAlignment = Element.ALIGN_CENTER }
        }
        table.addCell(Cell(Paragraph("$tableName ${comment ?: ""}", font)).apply {
            colspan = table.columns
        })
        table.addCell(buildCell("字段名称"))
        table.addCell(buildCell("数据类型"))
        table.addCell(buildCell("长度"))
        table.addCell(buildCell("可空"))
        table.addCell(buildCell("字段说明"))
        //table.endHeaders()
    }

    private fun buildTableRow(table: com.lowagie.text.Table, index: Int, tableFields: TableFields) {
        val font = Font(baseFont, 12F)
        val buildCell: (String?) -> Cell = { name ->
            Cell(Paragraph(name ?: "", font)).apply {
                verticalAlignment = Element.ALIGN_MIDDLE
            }
        }
        table.addCell(buildCell(tableFields.field.toUpperCase()))
        table.addCell(buildCell(tableFields.type))
        table.addCell(buildCell(tableFields.length).apply {
            horizontalAlignment = Element.ALIGN_RIGHT
        })
        table.addCell(buildCell(tableFields.nullable).apply {
            horizontalAlignment = Element.ALIGN_CENTER
        })
        table.addCell(buildCell(tableFields.comment ?: "  "))
    }
}