package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

class Trigger(val type: TriggerType,val actions:List<Action>) {


    constructor(type: String,actions:List<Map<*,*>>) : this(TriggerType.match(type),actions.map {
        val type = it.get("type")
        ActionType.match(type as? String).clazz.getConstructor(String::class.java).newInstance(it.get("call"))
    })


    fun call(player: CtOnlineRewardProxyPlayer){
        actions.forEach {
            it.call(player)
        }
    }

}