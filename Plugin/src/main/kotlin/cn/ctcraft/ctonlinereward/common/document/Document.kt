package cn.ctcraft.ctonlinereward.common.document



interface Document {


    fun generateDoc(): DocumentFile?{
        return null
    }

    fun generateDocs(): List<DocumentFile>{
        return emptyList()
    }

}