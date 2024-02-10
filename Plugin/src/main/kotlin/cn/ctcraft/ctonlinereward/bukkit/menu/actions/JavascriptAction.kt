package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.script.CompiledScript

class JavascriptAction(override val values: List<String>) : Action<String> {

    constructor(value:String):this(arrayListOf(value))

    val compiledScript = CompiledScript("""
        function __main__(){
           ${values.joinToString("\n")+'\n'}
        }    
    """.trimIndent())

    override fun call(player: CtOnlineRewardProxyPlayer?, map: Map<String, Any>?): Any? {
        return compiledScript.invoke("__main__", map)
    }

}