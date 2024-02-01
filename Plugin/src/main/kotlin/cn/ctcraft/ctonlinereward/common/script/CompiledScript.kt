package cn.ctcraft.ctonlinereward.common.script

import cn.ctcraft.ctonlinereward.common.logger.Logging
import cn.hutool.core.util.StrUtil
import java.io.File
import java.io.Reader
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.SimpleBindings

open class CompiledScript {

    private var compiledScript: javax.script.CompiledScript

    val scriptEngine: ScriptEngine

    constructor(reader: Reader){
        scriptEngine = ScriptManager.engine.getEngine()
        loadLib()
        compiledScript = ScriptManager.engine.compile(reader)
        magicFunction()
    }

    constructor(file: File){
        scriptEngine = ScriptManager.engine.getEngine()
        ScriptManager.enableRequire(scriptEngine,file.parentFile)
        loadLib()
        file.reader().use {
            compiledScript = ScriptManager.engine.compile(scriptEngine,it)
        }
        kotlin.runCatching {
            magicFunction()
        }.onFailure {
            if (it.message?.contains("Module not found") == true){
                Logging.severe("执行 ${file.name} 文件时,没有找到模块: ${StrUtil.subAfter(it.message,"Module not found:",true)}")
                Logging.severe("使用 /cor script reload <${file.name}> 可重新加载此脚本文件")
            }else{
                it.printStackTrace()
            }
        }
    }

    constructor(script: String) {
        scriptEngine = ScriptManager.engine.getEngine()
        loadLib()
        compiledScript = ScriptManager.engine.compile(scriptEngine, script)
        magicFunction()
    }


    fun eval(map: Map<String,Any>?){
        if (map != null) {
            compiledScript.eval(SimpleBindings(map))
        }else {
            compiledScript.eval()
        }
    }

    open fun loadLib(){
        scriptEngine.eval("""
            
            
        """.trimIndent())
    }

    /**
     * 执行脚本中的指定函数
     *
     * @param function 函数名
     * @param map 传入的默认对象
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun invoke(function: String, map: Map<String, Any>?, vararg args: Any): Any? {
        return ScriptManager.engine.invoke(this, function, map, *args)
    }

    /**
     * 执行脚本中的指定函数
     *
     * @param function 函数名
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun simpleInvoke(function: String, vararg args: Any?): Any? {
        return (scriptEngine as Invocable).invokeFunction(function, *args)
    }



    private fun magicFunction() {
        eval(null)
        scriptEngine.eval(
            """
            function IScriptNumberOne() {}
            IScriptNumberOne.prototype = this
            function newObject() { return new IScriptNumberOne() }
        """
        )
    }
}