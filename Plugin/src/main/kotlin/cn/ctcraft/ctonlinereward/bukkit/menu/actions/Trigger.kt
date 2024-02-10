package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

class Trigger(val type: TriggerType, val actions: List<Action<*>>) {

    companion object {
        @JvmStatic
        fun create(type: TriggerType, actions: List<Action<*>>): Trigger {
            return Trigger(type, actions)
        }

    }

    constructor(type: String, actions: List<Map<*, *>>) : this(TriggerType.match(type), ActionUtil.buildAction(actions))


    fun call(player: CtOnlineRewardProxyPlayer, map: Map<String, Any>?): Any? {
        var result: Any? = null
        actions.forEach {
            result = it.call(player, map)
        }
        return result
    }

}