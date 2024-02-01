package cn.ctcraft.ctonlinereward.bukkit.menu

import cn.ctcraft.ctonlinereward.common.CtOnlineRewardProxyPlayer
import java.util.concurrent.CopyOnWriteArrayList

class PublicSession(override val menu:Menu):Session {

    val member = CopyOnWriteArrayList<CtOnlineRewardProxyPlayer>()

    init {
        menu.target.link(menu)
    }

    fun addPlayer(player:CtOnlineRewardProxyPlayer){
        player.openMenu(menu)
        member.add(player)
    }

}