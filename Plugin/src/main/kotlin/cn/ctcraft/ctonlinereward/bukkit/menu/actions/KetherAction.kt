package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

class KetherAction(override val values: List<String>) :Action {


    constructor(value:String):this(arrayListOf(value))


    override fun call(player: CtOnlineRewardProxyPlayer) {
        KetherShell.eval(values, options = ScriptOptions.new {
            sender(player)
            set("player",player)
        })
    }
}