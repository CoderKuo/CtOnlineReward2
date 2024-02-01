package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import cn.ctcraft.ctonlinereward.common.script.CompiledScript

class JavascriptAction(override val values: List<String>) : Action {

    constructor(value:String):this(arrayListOf(value))

    val compiledScript = CompiledScript("""
        function wrap(){
           ${values.joinToString("\n")+'\n'}
        }    
    """.trimIndent())

    override fun call(player: CtOnlineRewardProxyPlayer) {
        compiledScript.invoke("wrap", mapOf("player" to player))
    }

}