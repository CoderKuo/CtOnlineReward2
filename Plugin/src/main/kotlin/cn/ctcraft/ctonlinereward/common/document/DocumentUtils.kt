package cn.ctcraft.ctonlinereward.common.document

import taboolib.library.configuration.ConfigurationSection
import java.util.concurrent.CopyOnWriteArrayList

import net.steppschuh.markdowngenerator.Markdown
import net.steppschuh.markdowngenerator.MarkdownElement
import net.steppschuh.markdowngenerator.image.Image
import net.steppschuh.markdowngenerator.link.Link
import net.steppschuh.markdowngenerator.list.TaskList
import net.steppschuh.markdowngenerator.list.TaskListItem
import net.steppschuh.markdowngenerator.list.UnorderedList
import net.steppschuh.markdowngenerator.list.UnorderedListItem
import net.steppschuh.markdowngenerator.progress.ProgressBar
import net.steppschuh.markdowngenerator.rule.HorizontalRule
import net.steppschuh.markdowngenerator.table.Table
import net.steppschuh.markdowngenerator.table.TableRow
import net.steppschuh.markdowngenerator.text.Text
import net.steppschuh.markdowngenerator.text.code.Code
import net.steppschuh.markdowngenerator.text.code.CodeBlock
import net.steppschuh.markdowngenerator.text.emphasis.BoldText
import net.steppschuh.markdowngenerator.text.emphasis.InsertedText
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText
import net.steppschuh.markdowngenerator.text.emphasis.MarkedText
import net.steppschuh.markdowngenerator.text.emphasis.StrikeThroughText
import net.steppschuh.markdowngenerator.text.emphasis.SubScriptText
import net.steppschuh.markdowngenerator.text.emphasis.SuperScriptText
import net.steppschuh.markdowngenerator.text.heading.Heading
import net.steppschuh.markdowngenerator.text.quote.QuoteBuilder


inline fun buildMarkdown(builder: MarkDownBuilder.()->Unit): String {
    return MarkDownBuilder().also(builder).toString()
}

class MarkDownBuilder: Markdown() {

    private val sb = StringBuffer()

    fun title(text:String,level:Int = 3,newLine:Boolean = true): MarkdownElement {
        return appendWithNewLine(Heading(text,level),newLine)
    }

    fun text(text: String,newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(Text(text),newLine)
    }

    fun code(text: String,newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(Code(text),newLine)
    }

    /**
     * 代码块
     * @param text 代码
     * @param language 语言
     * @param newLine 是否换行
     */
    fun codeBlock(text: String,language:String = "",newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(CodeBlock(text,language),newLine)
    }

    /**
     * 加粗文字
     * @param text 内容
     * @param newLine 是否换行
     */
    fun boldText(text: String,newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(BoldText(text),newLine)
    }

    /**
     * 水平线
     * @param length 长度 默认3
     * @param symbol 符号 默认为: -
     * @param newLine 是否换行
     *
     */
    fun hr(length:Int = 3, symbol: MarkdownHorizontalSymbol = MarkdownHorizontalSymbol.HYPHEN, newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(HorizontalRule(length,symbol.symbol),newLine)
    }

    /**
     * 换行符
     */
    fun br(): MarkdownElement {
        return appendWithNewLine(Text("\n"),false)
    }

    /**
     * 进度条
     */
    fun progressBar(value:Double,length:Int,newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(ProgressBar(value,length),newLine)
    }

    /**
     * 图片
     * @param text 图片描述
     * @param url 图片地址
     * @param newLine 是否换行
     */
    fun image(url:String,text:String = "",newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(Image(text,url),newLine)
    }

    /**
     * 超链接
     * @param url 连接
     * @param text 描述
     * @param newLine 是否换行
     */
    fun link(url:String,text: String = "",newLine: Boolean = true): Link {
        return Link(url, url).also {
            if(text.isEmpty()){
                appendWithNewLine(it,newLine)
            }else {
                appendWithNewLine(it, newLine)
            }
        }
    }

    /**
     * 任务列表
     * @param newLine 是否换行
     */
    fun taskList(newLine:Boolean = true,builder: TaskListItemBuilder.()->Unit): MarkdownElement {
        return appendWithNewLine(TaskListItemBuilder().also(builder).build(),newLine)
    }

    /**
     * 无序列表
     * @param newLine 是否换行
     */
    fun unorderList(newLine: Boolean = true,builder:UnorderListItemBuilder.()->Unit): MarkdownElement {
        return appendWithNewLine(UnorderListItemBuilder().also(builder).build(),newLine).also { br() }
    }

    /**
     * 表格
     * @param newLine 是否换行
     */
    fun table(newLine: Boolean = true,builder: TableBuilder.()->Unit): MarkdownElement {
        return appendWithNewLine(TableBuilder().also(builder).build(),newLine)
    }

    /**
     * 插入的文本
     */
    fun insertedText(text: String,newLine: Boolean = true): MarkdownElement {
        return appendWithNewLine(InsertedText(text),newLine)
    }

    /**
     * 倾斜文本
     */
    fun italicText(text: Any,newLine: Boolean = true):MarkdownElement{
        return appendWithNewLine(ItalicText(text),newLine)
    }

    /**
     * 强调文本
     */
    fun markedText(text: Any,newLine: Boolean=true):MarkdownElement{
        return appendWithNewLine(MarkedText(text),newLine)
    }

    /**
     * 删除文本
     */
    fun strikeThroughText(text: Any,newLine: Boolean = true):MarkdownElement{
        return appendWithNewLine(StrikeThroughText(text),newLine)
    }


    fun subScriptText(text: Any,newLine: Boolean = true):MarkdownElement{
        return appendWithNewLine(SubScriptText(text),newLine)
    }

    fun superScriptText(text: Any,newLine: Boolean = true):MarkdownElement{
        return appendWithNewLine(SuperScriptText(text),newLine)
    }

    /**
     * 引用
     */
    fun quote(newLine: Boolean = true,builder: cn.ctcraft.ctonlinereward.common.document.QuoteBuilder.()->Unit):MarkdownElement{
        return appendWithNewLine(cn.ctcraft.ctonlinereward.common.document.QuoteBuilder().also(builder).build(),newLine)
    }

    private fun appendWithNewLine(element: MarkdownElement, newLine: Boolean): MarkdownElement {
        sb.append(element)
        if (newLine) {
            sb.append("\n")
        }
        return element
    }

    override fun toString(): String {
        return sb.toString()
    }

    enum class MarkdownHorizontalSymbol(val symbol:Char){
        HYPHEN('-'),
        ASTERISK('*'),
        UNDERSCORE('_')
    }


}

class QuoteBuilder(){

    private val list = CopyOnWriteArrayList<String>()

    fun add(vararg str:String){
        list.addAll(str)
    }


    fun build():MarkdownElement{
        QuoteBuilder().apply {
            list.forEach {
                this.append(it).append("\n")
            }
            return this.build()
        }
    }

}

class TableBuilder{

    private val list = CopyOnWriteArrayList<TableRow<Any>>()

    private lateinit var alignment: List<TableAlign>

    private var limit: Int = -1

    fun add(vararg value: Any?){
        value.map {
            if (it is String){
                it.toString()
            }
            it
        }.also {
            list.add(TableRow(it))
        }
    }

    fun withAlignment(vararg alignment:TableAlign){
        this.alignment = alignment.toList()
    }

    fun limit(limit:Int){
        this.limit = limit
    }


    fun build(): Table {
        Table.Builder().apply {
            if (::alignment.isInitialized){
                this.withAlignments(alignment.map { it.code })
            }
            list.forEach {
                this.addRow(it)
            }
            if (limit != -1){
                this.withRowLimit(limit)
            }
            return build()
        }
    }

    enum class TableAlign(val code:Int){
        center(1),
        left(2),
        right(3)
    }
}

class UnorderListItemBuilder{
    private val list = CopyOnWriteArrayList<UnorderedListItem>()

    fun add(value: Any){
        list.add(UnorderedListItem(value))
    }

    fun build():UnorderedList<Any>{
        return UnorderedList(list as List<Any>?)
    }
}

class TaskListItemBuilder{

    private val list = CopyOnWriteArrayList<TaskListItem>()

    fun add(task: Any,checked:Boolean){
        list.add(TaskListItem(task,checked))
    }

    fun build():TaskList{
        return TaskList(list)
    }


}