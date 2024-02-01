package cn.ctcraft.ctonlinereward.bukkit.menu.actions

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer

interface Action {

    val values:List<String>

    fun call(player:CtOnlineRewardProxyPlayer)

}