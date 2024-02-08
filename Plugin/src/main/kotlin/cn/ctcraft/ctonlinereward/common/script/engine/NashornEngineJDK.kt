package cn.ctcraft.ctonlinereward.common.script.engine

import cn.ctcraft.ctonlinereward.common.script.CompiledScript
import com.github.alanger.commonjs.FilesystemFolder
import com.github.alanger.commonjs.Require
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine

class NashornEngineJDK:AbstractScriptEngine() {

    override fun getEngine(args: Array<String>, classLoader: ClassLoader): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args,classLoader)
    }

    override fun invoke(
        compiledScript: CompiledScript,
        function: String,
        map: Map<String, Any>?,
        vararg args: Any
    ): Any? {
        val newObject: ScriptObjectMirror = (compiledScript as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.let {
            (newObject.proto as ScriptObjectMirror).putAll(it)
        }

        return newObject.callMember(function,*args)
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        return func is ScriptObjectMirror && func.isFunction
    }

    override fun withRequire(engine: ScriptEngine, file: File) {
        Require.enable(engine, FilesystemFolder.create(file,"UTF-8"))
    }
}