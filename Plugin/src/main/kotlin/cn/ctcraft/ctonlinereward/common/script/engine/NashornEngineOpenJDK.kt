package cn.ctcraft.ctonlinereward.common.script.engine

import cn.ctcraft.ctonlinereward.common.script.CompiledScript
import com.github.alanger.commonjs.AbstractModule
import com.github.alanger.commonjs.FilesystemFolder
import com.github.alanger.commonjs.ModuleCache
import com.github.alanger.commonjs.Require
import com.github.alanger.commonjs.nashorn.NashornModule
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
class NashornEngineOpenJDK:AbstractScriptEngine() {

    override fun getEngine(args: Array<String>, classLoader: ClassLoader): ScriptEngine {

        return org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory().getScriptEngine(args,classLoader)
    }

    override fun invoke(
        compiledScript: CompiledScript,
        function: String,
        map: Map<String, Any>?,
        vararg args: Any
    ): Any? {
        val newObject:  org.openjdk.nashorn.api.scripting.ScriptObjectMirror =
            (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as  org.openjdk.nashorn.api.scripting.ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        return func is org.openjdk.nashorn.api.scripting.ScriptObjectMirror && func.isFunction
    }


    override fun withRequire(engine: ScriptEngine, file: File) {
        val bindings = engine.getBindings(100)

        val module = engine.eval("({})")
        val exports = engine.eval("({})")
        val created = NashornModule(engine,FilesystemFolder.create(file,"UTF-8"), ModuleCache(),"<main>",module,exports,null,null)
        bindings.put("require",created)
        bindings.put("module",module)
        bindings.put("exports",exports)
    }
}