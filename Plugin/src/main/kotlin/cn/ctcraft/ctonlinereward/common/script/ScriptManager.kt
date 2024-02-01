package cn.ctcraft.ctonlinereward.common.script

import cn.ctcraft.ctonlinereward.CtOnlineReward
import cn.ctcraft.ctonlinereward.common.logger.Logging
import cn.ctcraft.ctonlinereward.common.script.EngineType.*
import cn.ctcraft.ctonlinereward.common.script.engine.AbstractScriptEngine
import cn.ctcraft.ctonlinereward.common.script.engine.NashornEngineJDK
import cn.ctcraft.ctonlinereward.common.script.engine.NashornEngineOpenJDK
import cn.hutool.core.io.FileUtil
import com.github.alanger.commonjs.ModuleException
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.script.ScriptEngine

object ScriptManager {

    val scripts = ConcurrentHashMap<String,File>()

    val type  = CtOnlineReward.config.getEnum("script.engine",EngineType::class.java) ?: default
    val engine = when(type){
        nashorn->{
            when{
                check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") && ((System.getProperty("java.class.version")
                    .toDoubleOrNull() ?: 0.0) < 55.0) -> NashornEngineJDK()
                else->NashornEngineOpenJDK()
            }
        }
        default->{
            when{
                check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") && ((System.getProperty("java.class.version")
                    .toDoubleOrNull() ?: 0.0) < 55.0) -> NashornEngineJDK()
                else->NashornEngineOpenJDK()
            }
        }

    }


    fun check(className:String):Boolean{
        return try {
            Class.forName(className)
            true
        }catch (e:Exception){
            false
        }
    }

    fun runScriptFile(name:String,function:String? = null,map: Map<String, Any>? = null,
                      vararg args: Any){
        if (function == null){
            scripts[name]?.also {
                CompiledScript(it)
            }
        }else{
            scripts[name]?.also {
                engine.invoke(CompiledScript(it),function, map, args)
            }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun loadScripts(){
        scripts.clear()
        File(getDataFolder(),"script").listFiles().forEach {parent->
            loadScript(parent)
        }
    }

    fun loadScript(file: File){
        scripts.remove(file.name.removeSuffix(".js"))
        if (FileUtil.extName(file) != "js" && !file.isDirectory){
            return
        }

        val newFile = if (file.isDirectory){
            getMainFile(file) ?: return
        }else{
            file
        }
        scripts[FileUtil.mainName(file)] = newFile
        Logging.info("脚本文件 ${FileUtil.mainName(file)} 已载入")
    }


    /**
     * 获取入口文件
     * @param dir 文件夹对象 必须为文件夹
     */
    fun getMainFile(dir:File): File? {
        val mainFileNames = listOf("main.js","index.js","master.js","start.js")

        mainFileNames.forEach{
            File(dir,it).also {
                if (it.exists()){
                    return it
                }
            }
        }
        return null
    }

    fun enableRequire(engine: ScriptEngine,file: File){
        kotlin.runCatching {
            this.engine.withRequire(engine, file)
        }.onFailure {
            println(it)
        }
    }
}