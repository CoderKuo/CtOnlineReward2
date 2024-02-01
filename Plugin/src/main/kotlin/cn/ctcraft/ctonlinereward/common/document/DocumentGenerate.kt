package cn.ctcraft.ctonlinereward.common.document

import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependency
import taboolib.common.io.getInstance
import taboolib.common.io.runningClassesWithoutLibrary
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submitAsync
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object DocumentGenerate {

    val directory = File(getDataFolder(), "/document").apply {
        if (!this.exists()) {
            this.mkdirs()
        }
    }

    /**
     * fileName and Doc
     */
    val map = ConcurrentHashMap<String,DocumentFile>()

    @Awake(LifeCycle.ACTIVE)
    fun generateAllDoc() {
        submitAsync {
            runningClassesWithoutLibrary.filter {
                Document::class.java.isAssignableFrom(it) && it != Document::class.java
            }.forEach {
                (it.getInstance()?.get() as Document).also {
                    it.generateDoc()?.also {
                        docSave(it)
                    }
                    for (index in it.generateDocs().indices) {
                        it.generateDocs()[index].also { doc ->
                            docSave(doc)
                        }
                    }
                }
            }
            map.forEach {
                it.value.save()
            }
            map.clear()
        }
    }

    fun docSave(doc: DocumentFile) {
        map.getOrPut(doc.name){DocumentFile(doc.name,"")}.apply {
            append(doc.toString())
        }
    }

}