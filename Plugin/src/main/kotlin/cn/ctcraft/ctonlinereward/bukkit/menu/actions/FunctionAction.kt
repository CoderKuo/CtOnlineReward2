package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Function

class FunctionAction(override val values: CopyOnWriteArrayList<Function<Pair<CtOnlineRewardProxyPlayer?, Map<String, Any>>, Unit>>) :
    Action<Function<Pair<CtOnlineRewardProxyPlayer?, Map<String, Any>>, Unit>> {

    constructor() : this(CopyOnWriteArrayList())

    fun addFunc(func: Function<Pair<CtOnlineRewardProxyPlayer?, Map<String, Any>>, Unit>): FunctionAction {
        values.add(func)
        return this
    }

    override fun call(player: CtOnlineRewardProxyPlayer?, map: Map<String, Any>?): Any? {
        var result: Any? = null
        values.forEach {
            result = it.apply(player to (map ?: mutableMapOf()))
        }
        return result
    }


}