package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

class KetherAction(override val values: List<String>) : Action<String> {


    constructor(value:String):this(arrayListOf(value))


    override fun call(player: CtOnlineRewardProxyPlayer?, map: Map<String, Any>?): Any? {
        return KetherShell.eval(values, options = ScriptOptions.new {
            player?.let { sender(it) }
            map?.let { vars(it) }
        })
    }
}