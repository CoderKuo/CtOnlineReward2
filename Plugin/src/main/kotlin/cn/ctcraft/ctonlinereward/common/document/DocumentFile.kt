package cn.ctcraft.ctonlinereward.common.document

import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.config
import java.io.File

data class DocumentFile(val name:String,private val md:String, val file: File=File(DocumentGenerate.directory,"$name.md").also {

    if (!it.exists()){
        it.createNewFile()
    } }){

    private val sb:StringBuffer = StringBuffer(md)

    fun append(md: String){
        sb.append(md)
    }

    fun save(){
        file.writeText(sb.toString())
    }

    override fun toString(): String {
        return sb.toString()
    }
}