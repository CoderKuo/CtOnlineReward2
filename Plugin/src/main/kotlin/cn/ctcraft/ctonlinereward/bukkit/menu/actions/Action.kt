package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

interface Action<T> {

    val values: List<T>

    fun call(player: CtOnlineRewardProxyPlayer, map: Map<String, Any>)

}