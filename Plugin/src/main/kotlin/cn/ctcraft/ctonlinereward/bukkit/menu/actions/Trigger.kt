package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

class Trigger(val type: TriggerType, val actions: List<Action<*>>) {

    companion object {
        @JvmStatic
        fun create(type: TriggerType, actions: List<Action<*>>): Trigger {
            return Trigger(type, actions)
        }

    }

    constructor(type: String,actions:List<Map<*,*>>) : this(TriggerType.match(type),actions.map {
        val type = it.get("type")
        var call = it.get("call") ?: it.get("script") ?: it.get("value") ?: it.get("val")
        if (call == null) {
            call = it.entries.toList().let {
                it.get(it.indexOfFirst { it.key != "type" })
            }
        }
        if (call is List<*>) {
            ActionType.match(type as? String).clazz.getConstructor(List::class.java).newInstance(
                call
            )

        } else {
            ActionType.match(type as? String).clazz.getConstructor(String::class.java)
                .newInstance(call.toString())
        }
    })


    fun call(player: CtOnlineRewardProxyPlayer, map: Map<String, Any>) {
        actions.forEach {
            it.call(player, map)
        }
    }

}